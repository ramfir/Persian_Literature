package com.firdavs.persianliterature.author.db.mapper

import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.firdavs.persianliterature.author_api.model.Author

class AuthorsEntityToDomainMapperImpl : AuthorsEntityToDomainMapper {
    override fun mapTo(authors: List<AuthorEntity>): List<Author> {
        return authors.map {
            Author(
                id = it.id,
                name = it.name,
                imageUrl = it.imageUrl,
                birthDate = it.birthDate,
                deathDate = it.deathDate,
                birthPlace = it.birthPlace
            )
        }
    }

    override fun mapFrom(authors: List<Author>): List<AuthorEntity> {
        return authors.map {
            AuthorEntity(
                id = it.id,
                name = it.name,
                imageUrl = it.imageUrl,
                birthDate = it.birthDate,
                deathDate = it.deathDate,
                birthPlace = it.birthPlace
            )
        }
    }
}
