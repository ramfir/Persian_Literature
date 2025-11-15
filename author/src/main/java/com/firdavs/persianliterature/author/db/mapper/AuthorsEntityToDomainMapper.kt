package com.firdavs.persianliterature.author.db.mapper

import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.firdavs.persianliterature.author_api.model.Author

interface AuthorsEntityToDomainMapper {
    fun mapTo(authors: List<AuthorEntity>): List<Author>
    fun mapFrom(authors: List<Author>): List<AuthorEntity>
}
