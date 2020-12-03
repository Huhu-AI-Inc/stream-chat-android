package io.getstream.chat.docs.kotlin

import io.getstream.chat.android.client.channel.ChannelClient
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.Reaction
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.utils.ProgressCallback
import java.io.File

object Messages {
    val channelController: ChannelClient = TODO()
    val message: Message = TODO()

    fun sendAMessage() {
        val message = Message()
        message.text =
            "Josh I told them I was pesca-pescatarian. Which is one who eats solely fish who eat other fish."
        message.extraData["anotherCustomField"] = 234

        // add an image attachment to the message
        val attachment = Attachment()
        attachment.type = "image"
        attachment.imageUrl = "https://bit.ly/2K74TaG"
        attachment.fallback = "test image"
        // add some custom data to the attachment
        attachment.extraData["myCustomField"] = 123

        message.attachments.add(attachment)

        // include the user id of the mentioned user
        message.mentionedUsers.add(User("josh-id"))

        channelController.sendMessage(message).enqueue {
            val message = it.data()
        }
    }

    fun getAMessage() {
        channelController.getMessage("messageId").enqueue {
            val message = it.data()
        }
    }

    fun updateAMessage() {
        // update some field of the message
        message.text = "my updated text"
        // send the message to the channel
        channelController.updateMessage(message).enqueue {
            val message = it.data()
        }
    }

    fun deleteAMessage() {
        channelController.deleteMessage("messageId").enqueue {
            val deletedMessage = it.data()
        }
    }

    fun fileUploads() {
        val imageFile = File("path")
        val anyOtherFile = File("path")

        // upload an image
        channelController.sendImage(imageFile, object: ProgressCallback {
            override fun onSuccess(file: String) {

            }

            override fun onError(error: ChatError) {

            }

            override fun onProgress(progress: Long) {

            }
        })

        // upload a file
        channelController.sendFile(anyOtherFile, object: ProgressCallback{
            override fun onSuccess(file: String) {

            }

            override fun onError(error: ChatError) {

            }

            override fun onProgress(progress: Long) {

            }
        })
    }

    fun sendAReaction() {
        val reaction = Reaction("message-id", "like", 1)
        channelController.sendReaction(reaction).enqueue {
            val reaction = it.data()
        }
    }

    fun removeAReaction() {
        channelController.deleteReaction("message-id", "like").enqueue {
            val message = it.data()
        }
    }

    fun paginatingReactions() {
        // get the first 10 reactions
        channelController.getReactions("message-id", 0, 10).enqueue {
            val reactions = it.data()
        }

        // get the second 10 reactions
        channelController.getReactions("message-id", 10, 10).enqueue {
            val reactions = it.data()
        }

        // get 10 reactions after particular reaction
        val reactionId = "reaction-id"
        channelController.getReactions("message-id", reactionId, 10).enqueue {
            val reactions = it.data()
        }
    }

    fun cumulativeReactions() {
        val score = 5
        val reaction = Reaction("message-id", "like", score)
        channelController.sendReaction(reaction).enqueue {
            val reaction = it.data()
        }
    }
}