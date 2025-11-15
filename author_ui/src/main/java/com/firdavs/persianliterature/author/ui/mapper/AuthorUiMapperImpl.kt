package com.firdavs.persianliterature.author.ui.mapper

import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel

class AuthorUiMapperImpl : AuthorUiMapper {
    override fun map(input: List<Author>) = input.map {
        AuthorUiModel(
            id = it.id,
            photoUrl = it.imageUrl,
            fullName = it.name,
            birthDate = it.birthDate,
            deathDate = it.deathDate
        )
    }
}
