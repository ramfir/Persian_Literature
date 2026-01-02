package com.firdavs.persianliterature.author.db.mapper

import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.firdavs.persianliterature.author_api.model.Author

class AuthorsEntityToDomainMapperImpl : AuthorsEntityToDomainMapper {
    override fun mapTo(authors: List<AuthorEntity>): List<Author> {
        return authors.map {
            it.toDomain()
        }
    }

    override fun mapFrom(authors: List<Author>): List<AuthorEntity> {
        return authors.map {
            it.toDb()
        }
    }
}

fun AuthorEntity.toDomain() = Author(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl,
    born = this.born,
    died = this.died,
    place = this.place,
    bioUrl = this.bioUrl,
    isFavourite = this.isFavourite
)

fun Author.toDb() = AuthorEntity(
    id = this.id,
    name = this.name,
    imageUrl = this.imageUrl,
    born = this.born,
    died = this.died,
    place = this.place,
    bioUrl = this.bioUrl,
    isFavourite = this.isFavourite
)
