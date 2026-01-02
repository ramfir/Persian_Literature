package com.firdavs.persianliterature.author.repository

import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.mapper.toDomain
import com.firdavs.persianliterature.author.db.model.toDomain
import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouritesRepositoryImpl(
    private val authorsDao: AuthorsDao,
    private val worksDao: WorksDao
) : FavouritesRepository {

    override fun getFavouriteAuthors(): Flow<List<Author>> {
        return authorsDao.getFavouritesFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFavouriteWorks(): Flow<List<Work>> {
        return worksDao.getFavouritesFlow().map { entities ->
            entities.toDomain()
        }
    }

    override suspend fun toggleAuthorFavourite(id: String, isFavourite: Boolean) {
        authorsDao.updateFavourite(id, isFavourite)
    }

    override suspend fun toggleWorkFavourite(id: String, isFavourite: Boolean) {
        worksDao.updateFavourite(id, isFavourite)
    }
}
