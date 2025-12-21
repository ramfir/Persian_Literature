package com.firdavs.persianliterature.author.model

import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.google.firebase.firestore.PropertyName

data class WorkDTO(
    val id: String = "",
    @get:PropertyName("authorId") @set:PropertyName("authorId")
    var authorId: String = "",
    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",
    @get:PropertyName("publishYear") @set:PropertyName("publishYear")
    var publishYear: String? = null,
    @get:PropertyName("fileUrl") @set:PropertyName("fileUrl")
    var fileUrl: String = ""
)

fun List<WorkDTO>.toDb() = map { it.toDb() }

fun WorkDTO.toDb() = WorkEntity(
    id = id,
    authorId = authorId,
    title = title,
    publishYear = publishYear ?: "",
    fileUrl = fileUrl
)
