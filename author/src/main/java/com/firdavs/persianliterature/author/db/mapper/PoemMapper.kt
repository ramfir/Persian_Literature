package com.firdavs.persianliterature.author.db.mapper

import com.firdavs.persianliterature.author.db.model.PoemEntity
import com.firdavs.persianliterature.author_api.model.Poem

fun PoemEntity.toDomain() = Poem(
    id = id,
    title = title,
    text = text,
    author = author,
    isFavourite = isFavourite
)
