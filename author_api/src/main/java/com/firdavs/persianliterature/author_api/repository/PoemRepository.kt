package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.Poem

interface PoemRepository {
    suspend fun fetchPoems()
    suspend fun getRandomPoem(): Poem?
    suspend fun getPoemById(id: String): Poem?
}
