package com.firdavs.persianliterature.author.repository

import android.content.Context
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.mapper.AuthorsEntityToDomainMapper
import com.firdavs.persianliterature.author.db.mapper.toDomain
import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.firdavs.persianliterature.author.model.AuthorDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.AudioCacheStatus
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Author
import com.firdavs.persianliterature.author_api.model.AuthorWithWorks
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import com.firdavs.persianliterature.author.db.model.toDomain as workToDomain

class AuthorRepositoryImpl(
    private val authorsDao: AuthorsDao,
    private val authorsEntityToDomainMapper: AuthorsEntityToDomainMapper,
    private val languageManager: LanguageManager,
    private val context: Context
) : AuthorRepository {

    override suspend fun fetchAuthors() {
        val lang = languageManager.getSavedLanguage(context).firebaseCode
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

    override fun getAllAuthorsWithWorks(): Flow<List<AuthorWithWorks>> {
        return authorsDao.getAllAuthorsWithWorksFlow().map { rows ->
            rows.groupBy { it.id to it.name }
                .map { (author, worksRows) ->
                    AuthorWithWorks(
                        authorId = author.first,
                        authorName = author.second,
                        works = worksRows.mapNotNull { row ->
                            // Only create WorkEntity if work_id is not null (author has works)
                            row.work_id?.let {
                                WorkEntity(
                                    id = row.work_id,
                                    authorId = row.work_authorId ?: author.first,
                                    title = row.work_title ?: "",
                                    description = row.work_description,
                                    publishYear = row.work_publishYear,
                                    fileUrl = row.work_fileUrl,
                                    audioUrl = row.work_audioUrl,
                                    audioCacheStatus = row.work_audioCacheStatus?.let {
                                        AudioCacheStatus.valueOf(it)
                                    } ?: AudioCacheStatus.NOT_CACHED,
                                    audioContentLength = row.work_audioContentLength ?: 0L,
                                    audioCachedBytes = row.work_audioCachedBytes ?: 0L,
                                    audioDownloadStatus = row.work_audioDownloadStatus?.let {
                                        AudioDownloadStatus.valueOf(it)
                                    } ?: AudioDownloadStatus.NOT_DOWNLOADED,
                                    audioLocalPath = row.work_audioLocalPath,
                                    isFavourite = row.work_isFavourite ?: false
                                ).workToDomain()
                            }
                        }
                    )
                }
        }
    }
}
