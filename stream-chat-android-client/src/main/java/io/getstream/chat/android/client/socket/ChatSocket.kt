/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.chat.android.client.socket

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import io.getstream.chat.android.client.clientstate.DisconnectCause
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.errors.ChatErrorCode
import io.getstream.chat.android.client.errors.ChatNetworkError
import io.getstream.chat.android.client.events.ChatEvent
import io.getstream.chat.android.client.events.ConnectedEvent
import io.getstream.chat.android.client.logger.ChatLogger
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.network.NetworkStateProvider
import io.getstream.chat.android.client.parser.ChatParser
import io.getstream.chat.android.client.token.TokenManager
import io.getstream.chat.android.client.utils.stringify
import io.getstream.chat.android.core.internal.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.properties.Delegates

@Suppress("TooManyFunctions", "LongParameterList")
internal open class ChatSocket constructor(
    private val apiKey: String,
    private val wssUrl: String,
    private val tokenManager: TokenManager,
    private val socketFactory: SocketFactory,
    private val networkStateProvider: NetworkStateProvider,
    private val parser: ChatParser,
    private val coroutineScope: CoroutineScope,
) {
    private val logger = ChatLogger.get("ChatSocket")
    private var connectionConf: SocketFactory.ConnectionConf? = null
    private var socket: Socket? = null
    private var eventsParser: EventsParser? = null
    private var socketConnectionJob: Job? = null
    private val listeners = mutableSetOf<SocketListener>()
    private val eventUiHandler = Handler(Looper.getMainLooper())
    private val healthMonitor = HealthMonitor(
        reconnectCallback = {
            if (state is State.DisconnectedTemporarily) {
                this@ChatSocket.reconnect(connectionConf)
            }
        },
        checkCallback = {
            (state as? State.Connected)?.let {
                sendEvent(it.event)
            }
        }
    )

    private val networkStateListener = object : NetworkStateProvider.NetworkStateListener {
        override fun onConnected() {
            logger.logI("[onNetworkConnected] socket state: ${state.javaClass.simpleName}")
            if (state is State.DisconnectedTemporarily || state == State.NetworkDisconnected) {
                logger.logI("network connected, reconnecting socket")
                reconnect(connectionConf)
            }
        }

        override fun onDisconnected() {
            logger.logI("[onNetworkDisconnected] socket state: ${state.javaClass.simpleName}")
            healthMonitor.stop()
            if (state is State.Connected || state is State.Connecting) {
                state = State.NetworkDisconnected
            }
        }
    }

    private var reconnectionAttempts = 0

    @VisibleForTesting
    internal var state: State by Delegates.observable(
        State.DisconnectedTemporarily(null) as State
    ) { _, oldState, newState ->
        if (oldState != newState) {
            logger.logI("[updateState] newState: ${newState.javaClass.simpleName}")
            when (newState) {
                is State.Connecting -> {
                    healthMonitor.stop()
                    callListeners { it.onConnecting() }
                }
                is State.Connected -> {
                    healthMonitor.start()
                    callListeners { it.onConnected(newState.event) }
                }
                is State.NetworkDisconnected -> {
                    shutdownSocketConnection()
                    healthMonitor.stop()
                    callListeners { it.onDisconnected(DisconnectCause.NetworkNotAvailable) }
                }
                is State.DisconnectedByRequest -> {
                    shutdownSocketConnection()
                    healthMonitor.stop()
                    callListeners { it.onDisconnected(DisconnectCause.ConnectionReleased) }
                }
                is State.DisconnectedTemporarily -> {
                    shutdownSocketConnection()
                    healthMonitor.onDisconnected()
                    callListeners { it.onDisconnected(DisconnectCause.Error(newState.error)) }
                }
                is State.DisconnectedPermanently -> {
                    shutdownSocketConnection()
                    connectionConf = null
                    networkStateProvider.unsubscribe(networkStateListener)
                    healthMonitor.stop()
                    callListeners { it.onDisconnected(DisconnectCause.UnrecoverableError(newState.error)) }
                }
            }
        }
    }
        private set

    open fun onSocketError(error: ChatError) {
        logger.logE("[onSocketError] error: ${error.stringify()}")
        if (state !is State.DisconnectedPermanently) {
            logger.logE(error)
            callListeners { it.onError(error) }
            (error as? ChatNetworkError)?.let(::onChatNetworkError)
        }
    }

    private fun onChatNetworkError(error: ChatNetworkError) {
        logger.logE("[onChatNetworkError] error: ${error.stringify()}")
        if (ChatErrorCode.isAuthenticationError(error.streamCode)) {
            tokenManager.expireToken()
        }

        when (error.streamCode) {
            ChatErrorCode.PARSER_ERROR.code,
            ChatErrorCode.CANT_PARSE_CONNECTION_EVENT.code,
            ChatErrorCode.CANT_PARSE_EVENT.code,
            ChatErrorCode.UNABLE_TO_PARSE_SOCKET_EVENT.code,
            ChatErrorCode.NO_ERROR_BODY.code,
            -> {
                if (reconnectionAttempts < RETRY_LIMIT) {
                    coroutineScope.launch {
                        delay(DEFAULT_DELAY * reconnectionAttempts.toDouble().pow(2.0).toLong())
                        reconnect(connectionConf)
                        reconnectionAttempts += 1
                    }
                }
            }
            ChatErrorCode.UNDEFINED_TOKEN.code,
            ChatErrorCode.INVALID_TOKEN.code,
            ChatErrorCode.API_KEY_NOT_FOUND.code,
            ChatErrorCode.VALIDATION_ERROR.code,
            -> {
                state = State.DisconnectedPermanently(error)
            }
            else -> {
                state = State.DisconnectedTemporarily(error)
            }
        }
    }

    open fun removeListener(listener: SocketListener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    open fun addListener(listener: SocketListener) {
        synchronized(listeners) {
            listeners.add(listener)
        }
    }

    fun connectUser(user: User, isAnonymous: Boolean) {
        logger.logI("[connectUser] isAnonymous: $isAnonymous, user.id: ${user.id}")
        connect(
            when (isAnonymous) {
                true -> SocketFactory.ConnectionConf.AnonymousConnectionConf(wssUrl, apiKey, user)
                false -> SocketFactory.ConnectionConf.UserConnectionConf(wssUrl, apiKey, user)
            }
        )
    }

    fun reconnectUser(user: User, isAnonymous: Boolean) {
        logger.logI("[reconnectUser] isAnonymous: $isAnonymous, user.id: ${user.id}")
        reconnect(
            when (isAnonymous) {
                true -> SocketFactory.ConnectionConf.AnonymousConnectionConf(wssUrl, apiKey, user)
                false -> SocketFactory.ConnectionConf.UserConnectionConf(wssUrl, apiKey, user)
            }
        )
    }

    protected open fun connect(connectionConf: SocketFactory.ConnectionConf) {
        val isNetworkConnected = networkStateProvider.isConnected()
        logger.logI("[connect] isNetworkConnected: $isNetworkConnected")
        this.connectionConf = connectionConf
        if (isNetworkConnected) {
            setupSocket(connectionConf)
        } else {
            state = State.NetworkDisconnected
        }
        networkStateProvider.subscribe(networkStateListener)
    }

    open fun disconnect() {
        logger.logI("[disconnect] no args")
        reconnectionAttempts = 0
        state = State.DisconnectedPermanently(null)
    }

    open fun releaseConnection() {
        logger.logD("[releaseConnection] no args")
        state = State.DisconnectedByRequest
    }

    open fun onConnectionResolved(event: ConnectedEvent) {
        logger.logD("[releaseConnection] event.type: ${event.type}")
        state = State.Connected(event)
    }

    open fun onEvent(event: ChatEvent) {
        healthMonitor.ack()
        callListeners { listener -> listener.onEvent(event) }
    }

    internal open fun sendEvent(event: ChatEvent) {
        socket?.send(event)
    }

    private fun reconnect(connectionConf: SocketFactory.ConnectionConf?) {
        logger.logD("[reconnect] user.id: ${connectionConf?.user?.id}")
        shutdownSocketConnection()
        setupSocket(connectionConf?.asReconnectionConf())
    }

    private fun setupSocket(connectionConf: SocketFactory.ConnectionConf?) {
        val isNetworkConnected = networkStateProvider.isConnected()
        logger.logI("[setupSocket] isNetworkConnected: $isNetworkConnected, user.id: ${connectionConf?.user?.id}")
        state = when (isNetworkConnected) {
            true -> when (connectionConf) {
                null -> State.DisconnectedPermanently(null)
                is SocketFactory.ConnectionConf.AnonymousConnectionConf,
                is SocketFactory.ConnectionConf.UserConnectionConf,
                -> {
                    socketConnectionJob = coroutineScope.launch {
                        tokenManager.ensureTokenLoaded()
                        withContext(DispatcherProvider.Main) {
                            socket = socketFactory.createSocket(createNewEventsParser(), connectionConf)
                        }
                    }
                    State.Connecting
                }
            }
            else -> State.DisconnectedTemporarily(
                ChatNetworkError.create(
                    description = "Network is not available",
                    streamCode = ChatErrorCode.SOCKET_FAILURE.code,
                    statusCode = -1
                )
            )
        }
    }

    private fun createNewEventsParser(): EventsParser = EventsParser(parser, this).also {
        eventsParser = it
    }

    private fun shutdownSocketConnection() {
        logger.logD("[shutdownSocketConnection] no args")
        socketConnectionJob?.cancel()
        eventsParser?.closeByClient()
        eventsParser = null
        socket?.close(EventsParser.CODE_CLOSE_SOCKET_FROM_CLIENT, "Connection close by client")
        socket = null
    }

    private fun callListeners(call: (SocketListener) -> Unit) {
        synchronized(listeners) {
            listeners.forEach { listener ->
                eventUiHandler.post { call(listener) }
            }
        }
    }

    private companion object {
        private const val RETRY_LIMIT = 3
        private const val DEFAULT_DELAY = 500
    }

    @VisibleForTesting
    internal sealed class State {
        object Connecting : State() {
            override fun toString(): String = "Connecting"
        }

        data class Connected(val event: ConnectedEvent) : State()
        object NetworkDisconnected : State() {
            override fun toString(): String = "NetworkDisconnected"
        }

        data class DisconnectedTemporarily(val error: ChatNetworkError?) : State()
        data class DisconnectedPermanently(val error: ChatNetworkError?) : State()
        object DisconnectedByRequest : State() {
            override fun toString(): String = "DisconnectedByRequest"
        }
    }
}
