package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author_api.model.Poem
import com.firdavs.persianliterature.author_api.model.Work
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavouriteAuthors(): Flow<List<Author>>
    fun getFavouriteWorks(): Flow<List<Work>>
    fun getFavouritePoems(): Flow<List<Poem>>
    suspend fun toggleAuthorFavourite(id: String, isFavourite: Boolean)
    suspend fun toggleWorkFavourite(id: String, isFavourite: Boolean)
    suspend fun togglePoemFavourite(id: String, isFavourite: Boolean)
}
