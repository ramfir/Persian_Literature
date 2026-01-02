package com.firdavs.persianliterature.author.repository

import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapper
import com.firdavs.persianliterature.author.db.mapper.toDomain
import com.firdavs.persianliterature.author.model.AuthorDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AuthorRepositoryImpl(
    private val authorsDao: AuthorsDao,
    private val authorsEntityToDomainMapper: AuthorsEntityToDomainMapper
) : AuthorRepository {

    override suspend fun fetchAuthors() {
        val lang = "en" // Locale.getDefault().language for test purpose
        val authorsCollection = Firebase.firestore.collection("authors_$lang")
        val snapshot = authorsCollection.get(Source.SERVER).await()
        val authorsDTO = snapshot.documents.mapNotNull { document ->
            val authorDto = document.toObject(AuthorDTO::class.java)
            authorDto?.copy(id = document.id)
        }
        // Preserve existing favourite status
        val existingFavouriteIds = authorsDao.getFavouriteIds().toSet()
        val authorsWithFavourites = authorsDTO.toDb().map { author ->
            author.copy(isFavourite = author.id in existingFavouriteIds)
        }
        authorsDao.insert(authorsWithFavourites)
    }

    override fun getAuthors(): Flow<List<Author>> {
        return authorsDao.getAllFlow().map {
            authorsEntityToDomainMapper.mapTo(it)
        }
    }

    override fun getAuthor(id: String): Flow<Author> {
        return authorsDao.getByIdFlow(id).map {
            it.toDomain()
        }
    }
}
