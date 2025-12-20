package com.firdavs.persianliterature.author.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author_api.model.Work

@Entity(tableName = AuthorsDb.WORKS)
data class WorkEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val title: String,
    val publishYear: String,
    val fileUrl: String?
)

fun List<WorkEntity>.toDomain() = map { it.toDomain() }

fun WorkEntity.toDomain() = Work(
    id = id,
    authorId = authorId,
    title = title,
    publishYear = publishYear,
    fileUrl = fileUrl
)
