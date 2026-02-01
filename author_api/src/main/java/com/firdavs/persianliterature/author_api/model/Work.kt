package com.firdavs.persianliterature.author_api.model

data class Work(
    val id: String,
    val authorId: String,
    val title: String,
    val description: String? = null,
    val publishYear: String?,
    val fileUrl: String?,
    val audioUrl: String? = null,

    // New cache-related fields
    val audioCacheStatus: AudioCacheStatus = AudioCacheStatus.NOT_CACHED,
    val audioContentLength: Long = 0L,
    val audioCachedBytes: Long = 0L,

    // Deprecated - kept for backward compatibility during migration
    @Deprecated("Use audioCacheStatus instead")
    val audioDownloadStatus: AudioDownloadStatus = AudioDownloadStatus.NOT_DOWNLOADED,
    @Deprecated("No longer used - cache managed by ExoPlayer")
    val audioLocalPath: String? = null,
    val isFavourite: Boolean = false
)
