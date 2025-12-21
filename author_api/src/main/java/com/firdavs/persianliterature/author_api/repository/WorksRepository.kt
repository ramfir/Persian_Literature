package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.Work
import kotlinx.coroutines.flow.Flow

interface WorksRepository {
    suspend fun fetchWorks()
    fun getWorksByAuthorId(authorId: String): Flow<List<Work>>
    fun getWork(id: String): Flow<Work>
}
