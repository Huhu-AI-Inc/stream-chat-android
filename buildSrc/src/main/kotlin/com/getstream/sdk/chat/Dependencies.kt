package com.getstream.sdk.chat

private const val ACTIVITY_KTX_VERSION = "1.2.0-alpha07"
private const val ANDROID_BUILD_TOOL_VERSION = "4.0.1"
private const val ANDROID_JUNIT5_GRADLE_PLUGIN_VERSION = "1.6.2.0"
private const val ANDROID_MAVEN_GRADLE_PLUGIN_VERSION = "2.1"
private const val ANDROID_LEGACY_SUPPORT = "1.0.0"
private const val ANDROIDX_CORE_TEST_VERSION = "2.1.0"
private const val ANDROIDX_MEDIA_VERSION = "1.1.0"
private const val ANDROID_X_KTX_VERSION = "1.3.1"
private const val APP_COMPAT_VERSION = "1.1.0"
private const val COIL_VERSION = "0.11.0"
private const val CONSTRAINT_LAYOUT_VERSION = "2.0.0"
private const val COROUTINES_VERSION = "1.3.8"
private const val DEXTER_VERSION = "6.2.1"
private const val DOKKA_VERSION = "0.10.1"
private const val DRAWABLETOOLBOX_VERSION = "1.0.7"
private const val EXOPLAYER_VERSION = "2.9.6"
private const val FIREBASE_ANALYTICS_VERSION = "17.4.4"
private const val FIREBASE_CRASHLYTICS_VERSION = "17.1.1"
private const val FIREBASE_MESSAGING_VERSION = "20.2.4"
private const val FIREBASE_PLUGIN_VERSION = "2.2.0"
private const val FRAGMENT_KTX_VERSION = "1.3.0-alpha07"
private const val FRESCO_VERSION = "2.3.0"
private const val GITVERSIONER_VERSION = "0.5.0"
private const val GLIDE_VERSION = "4.11.0"
private const val GLIDE_REDIRECT_VERSION = "2.0.1"
private const val GOOGLE_SERVICES_VERSION = "4.3.3"
private const val GRADLE_VERSIONS_PLUGIN = "0.29.0"
private const val GSON_VERSION = "2.8.6"
private const val JACOCO_VERSION = "0.2"
private const val JUNIT5_VERSION = "5.6.2"
private const val KEYBOARD_VISIBILITY_EVENT_VERSION = "2.3.0"
private const val KLUENT_VERSION = "1.61"
private const val KOIN_VERSION = "2.1.6"
private const val KOTLIN_VERSION = "1.3.72"
private const val KTLINT_VERSION = "9.3.0"
private const val LEAK_CANARY_VERSION = "2.4"
private const val LIFECYCLE_EXTENSION_VERSION = "2.2.0"
private const val LIFECYCLE_VIEWMODEL_KTX_VERSION = "2.2.0"
private const val MATERIAL_COMPONENTS_VERSION = "1.1.0"
private const val MARWON_VERSION = "4.4.0"
private const val MOKITO_KOTLIN_VERSION = "2.2.0"
private const val MOKITO_VERSION = "3.4.6"
private const val NAVIGATION_VERSION = "2.3.0"
private const val OKHTTP_VERSION = "4.8.0"
private const val PHOTODRAWEEVIEW_VERSION = "1.1.0"
private const val PREFERENCES_VERSION = "1.1.1"
private const val TIMBER_VERSION = "4.7.1"
private const val RECYCLERVIEW_VERSION = "1.2.0-alpha05"
private const val RETROFIT_VERSION = "2.9.0"
private const val ROOM_VERSION = "2.2.5"
private const val STREAM_LIVEDATA_VERSION = "0.7.7"

object Dependencies {
    val activityKtx = "androidx.activity:activity-ktx:$ACTIVITY_KTX_VERSION"
    val androidBuildToolGradlePlugin = "com.android.tools.build:gradle:$ANDROID_BUILD_TOOL_VERSION"
    val androidJunit5GradlePlugin = "de.mannodermaus.gradle.plugins:android-junit5:$ANDROID_JUNIT5_GRADLE_PLUGIN_VERSION"
    val androidMavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:$ANDROID_MAVEN_GRADLE_PLUGIN_VERSION"
    val androidLegacySupport = "androidx.legacy:legacy-support-v4:$ANDROID_LEGACY_SUPPORT"
    val androidxCoreTest = "androidx.arch.core:core-testing:$ANDROIDX_CORE_TEST_VERSION"
    val androidXKTX = "androidx.core:core-ktx:$ANDROID_X_KTX_VERSION"
    val androidxMedia = "androidx.media:media:$ANDROIDX_MEDIA_VERSION"
    val appCompat = "androidx.appcompat:appcompat:$APP_COMPAT_VERSION"
    val coil = "io.coil-kt:coil:$COIL_VERSION"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:$CONSTRAINT_LAYOUT_VERSION"
    val dexter = "com.karumi:dexter:$DEXTER_VERSION"
    val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:$DOKKA_VERSION"
    val drawabletoolbox = "com.github.duanhong169:drawabletoolbox:$DRAWABLETOOLBOX_VERSION"
    val exoplayerCore = "com.google.android.exoplayer:exoplayer-core:$EXOPLAYER_VERSION"
    val exoplayerDash = "com.google.android.exoplayer:exoplayer-dash:$EXOPLAYER_VERSION"
    val exoplayerHls = "com.google.android.exoplayer:exoplayer-hls:$EXOPLAYER_VERSION"
    val exoplayerSmoothstreaming = "com.google.android.exoplayer:exoplayer-smoothstreaming:$EXOPLAYER_VERSION"
    val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx:$FIREBASE_ANALYTICS_VERSION"
    val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:$FIREBASE_CRASHLYTICS_VERSION"
    val firebaseMessaging = "com.google.firebase:firebase-messaging:$FIREBASE_MESSAGING_VERSION"
    val firebasePlugin = "com.google.firebase:firebase-crashlytics-gradle:$FIREBASE_PLUGIN_VERSION"
    val fragmentKtx = "androidx.fragment:fragment-ktx:$FRAGMENT_KTX_VERSION"
    val fresco = "com.facebook.fresco:fresco:$FRESCO_VERSION"
    val gitversionerPlugin = "com.pascalwelsch.gitversioner:gitversioner:$GITVERSIONER_VERSION"
    val glide = "com.github.bumptech.glide:glide:$GLIDE_VERSION"
    val glideCompiler = "com.github.bumptech.glide:compiler:$GLIDE_VERSION"
    val glideRedirect = "com.aminography:redirectglide:$GLIDE_REDIRECT_VERSION"
    val googleServicesPlugin = "com.google.gms:google-services:$GOOGLE_SERVICES_VERSION"
    val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:$GRADLE_VERSIONS_PLUGIN"
    val gson = "com.google.code.gson:gson:$GSON_VERSION"
    val gsonConverter = "com.squareup.retrofit2:converter-gson:$RETROFIT_VERSION"
    val jacoco = "com.hiya:jacoco-android:$JACOCO_VERSION"
    val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:$JUNIT5_VERSION"
    val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:$JUNIT5_VERSION"
    val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$JUNIT5_VERSION"
    val keyboardVisibilityEvent = "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:$KEYBOARD_VISIBILITY_EVENT_VERSION"
    val kluent = "org.amshove.kluent:kluent:$KLUENT_VERSION"
    val koinAndroidxExt = "org.koin:koin-androidx-ext:$KOIN_VERSION"
    val koinAndroidxScope = "org.koin:koin-androidx-scope:$KOIN_VERSION"
    val koinAndroidxViewmodel = "org.koin:koin-androidx-viewmodel:$KOIN_VERSION"
    val koinCore = "org.koin:koin-core:$KOIN_VERSION"
    val koinCoreExt = "org.koin:koin-core-ext:$KOIN_VERSION"
    val koinTest = "org.koin:koin-test:$KOIN_VERSION"
    val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"
    val kotlinSTDLib = "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION"
    val ktlintPlugin = "org.jlleitschuh.gradle:ktlint-gradle:$KTLINT_VERSION"
    val leakCanary = "com.squareup.leakcanary:leakcanary-android:$LEAK_CANARY_VERSION"
    val lifecycleExtension = "androidx.lifecycle:lifecycle-extensions:$LIFECYCLE_EXTENSION_VERSION"
    val lifecycleViewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:$LIFECYCLE_VIEWMODEL_KTX_VERSION"
    val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$OKHTTP_VERSION"
    val materialComponents = "com.google.android.material:material:$MATERIAL_COMPONENTS_VERSION"
    val marwonCore = "io.noties.markwon:core:$MARWON_VERSION"
    val marwonLinkify = "io.noties.markwon:linkify:$MARWON_VERSION"
    val marwonextStrikethrough = "io.noties.markwon:ext-strikethrough:$MARWON_VERSION"
    val marwonImage = "io.noties.markwon:image:$MARWON_VERSION"
    val mockito = "org.mockito:mockito-core:$MOKITO_VERSION"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:$MOKITO_KOTLIN_VERSION"
    val navigationFragmentKTX = "androidx.navigation:navigation-fragment-ktx:$NAVIGATION_VERSION"
    val navigationSafeArgsGradlePlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:$NAVIGATION_VERSION"
    val navigationRuntimeKTX = "androidx.navigation:navigation-runtime-ktx:$NAVIGATION_VERSION"
    val navigationUIKTX = "androidx.navigation:navigation-ui-ktx:$NAVIGATION_VERSION"
    val okhttp = "com.squareup.okhttp3:okhttp:$OKHTTP_VERSION"
    val photodraweeview = "me.relex:photodraweeview:$PHOTODRAWEEVIEW_VERSION"
    val preferences = "androidx.preference:preference:$PREFERENCES_VERSION"
    val timber = "com.jakewharton.timber:timber:$TIMBER_VERSION"
    val recyclerview = "androidx.recyclerview:recyclerview:$RECYCLERVIEW_VERSION"
    val retrofit = "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION"
    val roomRuntime = "androidx.room:room-runtime:$ROOM_VERSION"
    val streamLivedata = "com.github.GetStream:stream-chat-android-livedata:$STREAM_LIVEDATA_VERSION"

    @JvmStatic
    fun isNonStable(version: String): Boolean = isStable(version).not()

    @JvmStatic
    fun isStable(version: String): Boolean = ("^[0-9.]+$").toRegex().matches(version)
}
