package com.firdavs.persianliterature.author.ui.mapper

import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel

class AuthorUiMapperImpl : AuthorUiMapper {
    override fun map(input: List<Author>) = input.map {
        it.toUi()
    }
}

fun Author.toUi() = AuthorUiModel(
    id = this.id,
    photoUrl = this.imageUrl,
    fullName = this.name,
    birthDate = this.birthDate,
    deathDate = this.deathDate,
    bioUrl = this.bioUrl
)
