package com.firdavs.persianliterature.settings.api

/** Provides localized "what's new" notes for app versions. */
interface ReleaseNotesRepository {
    /**
     * Returns release notes for versions in range (fromVersionCode, toVersionCode],
     * newest first. Returns empty list if nothing found or on error.
     */
    suspend fun getReleaseNotes(fromVersionCode: Int, toVersionCode: Int): List<ReleaseNotes>
}

/** Changes introduced in a single app version. */
data class ReleaseNotes(
    val versionCode: Int,
    val versionName: String,
    val items: List<String>
)
