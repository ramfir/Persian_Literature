package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author_api.model.AuthorWithWorks
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    suspend fun fetchAuthors()
    fun getAuthors(): Flow<List<Author>>
    fun getAuthor(id: String): Flow<Author>
    fun getAllAuthorsWithWorks(): Flow<List<AuthorWithWorks>>
    fun getAuthorsWithAtLeastTwoWorks(): Flow<List<AuthorWithWorks>>
}
