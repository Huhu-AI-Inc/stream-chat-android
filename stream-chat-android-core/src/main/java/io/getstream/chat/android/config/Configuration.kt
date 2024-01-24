package io.getstream.chat.android.config

public object Configuration {
    public const val compileSdk: Int = 34
    public const val targetSdk: Int = 34
    public const val sampleTargetSdk: Int = 34
    public const val minSdk: Int = 21
    public const val majorVersion: Int = 6
    public const val minorVersion: Int = 0
    public const val patchVersion: Int = 8
    public const val versionName: String = "$majorVersion.$minorVersion.$patchVersion"
    public const val snapshotVersionName: String = "$majorVersion.$minorVersion.${patchVersion + 1}-SNAPSHOT"
    public const val artifactGroup: String = "io.getstream"
}

