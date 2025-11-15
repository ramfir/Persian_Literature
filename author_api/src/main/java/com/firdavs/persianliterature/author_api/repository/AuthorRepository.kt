package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.Author
import kotlinx.coroutines.flow.Flow

interface AuthorRepository {
    suspend fun fetchAuthors()
    fun getAuthors(): Flow<List<Author>>
}
