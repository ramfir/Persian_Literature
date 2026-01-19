package com.firdavs.persianliterature.author.model

import com.firdavs.persianliterature.author.db.model.PoemEntity
import com.google.firebase.firestore.PropertyName

data class PoemDTO(
    val id: String = "",
    @get:PropertyName("title") @set:PropertyName("title")
    var title: String = "",
    @get:PropertyName("text") @set:PropertyName("text")
    var text: String = "",
    @get:PropertyName("author") @set:PropertyName("author")
    var author: String = ""
)

fun List<PoemDTO>.toDb() = map { it.toDb() }

fun PoemDTO.toDb() = PoemEntity(
    id = id,
    title = title,
    text = text,
    author = author
)
