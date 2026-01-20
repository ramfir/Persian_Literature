package com.firdavs.persianliterature.author.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Work

@Entity(tableName = AuthorsDb.WORKS)
data class WorkEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val title: String,
    val description: String? = null,
    val publishYear: String?,
    val fileUrl: String?,
    val audioUrl: String? = null,
    val audioDownloadStatus: AudioDownloadStatus = AudioDownloadStatus.NOT_DOWNLOADED,
    val audioLocalPath: String? = null,
    val isFavourite: Boolean = false
)

fun List<WorkEntity>.toDomain() = map { it.toDomain() }

fun WorkEntity.toDomain() = Work(
    id = id,
    authorId = authorId,
    title = title,
    description = description,
    publishYear = publishYear,
    fileUrl = fileUrl,
    audioUrl = audioUrl,
    audioDownloadStatus = audioDownloadStatus,
    audioLocalPath = audioLocalPath,
    isFavourite = isFavourite
)
