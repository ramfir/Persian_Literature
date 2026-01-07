package com.firdavs.persianliterature.author.repository

import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.firdavs.persianliterature.author.db.model.toDomain
import com.firdavs.persianliterature.author.model.WorkDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class WorksRepositoryImpl(
    private val worksDao: WorksDao
) : WorksRepository {
    override suspend fun fetchWorks() {
        val lang = "en" // Locale.getDefault().language
        val worksCollection = Firebase.firestore.collection("works_$lang")
        val snapshot = worksCollection.get(Source.SERVER).await()
        val worksDTO = snapshot.documents.mapNotNull { document ->
            val workDto = document.toObject(WorkDTO::class.java)
            workDto?.copy(id = document.id)
        }
        // Preserve existing favourite status
        val existingFavouriteIds = worksDao.getFavouriteIds().toSet()

        // Preserve existing audio download info
        val existingAudioInfo = worksDao.getDownloadedAudioInfo().associateBy { it.id }

        val worksWithPreservedData = worksDTO.toDb().map { work ->
            val audioInfo = existingAudioInfo[work.id]
            work.copy(
                isFavourite = work.id in existingFavouriteIds,
                audioDownloadStatus = audioInfo?.audioDownloadStatus ?: work.audioDownloadStatus,
                audioLocalPath = audioInfo?.audioLocalPath ?: work.audioLocalPath
            )
        }
        worksDao.insert(worksWithPreservedData)
    }

    override fun getWorksByAuthorId(authorId: String): Flow<List<Work>> {
        return worksDao.getByAuthorIdFlow(authorId).map { works: List<WorkEntity> ->
            works.toDomain()
        }
    }

    override fun getWork(id: String): Flow<Work> {
        return worksDao.getByIdFlow(id).map {
            it.toDomain()
        }
    }

    override suspend fun updateAudioDownloadStatus(
        workId: String,
        status: AudioDownloadStatus,
        localPath: String?
    ) {
        if (localPath != null) {
            worksDao.updateAudioDownloadStatus(workId, status, localPath)
        } else {
            worksDao.updateAudioDownloadStatusOnly(workId, status)
        }
    }

    override fun getWorksWithDownloadedAudio(): Flow<List<Work>> {
        return worksDao.getWorksWithDownloadedAudio().map { works ->
            works.toDomain()
        }
    }
}
