package com.firdavs.persianliterature.author_api.model

data class Work(
    val id: String,
    val authorId: String,
    val title: String,
    val publishYear: String,
    val fileUrl: String?,
    val audioUrl: String? = null,
    val audioDownloadStatus: AudioDownloadStatus = AudioDownloadStatus.NOT_DOWNLOADED,
    val audioLocalPath: String? = null,
    val isFavourite: Boolean = false
)
