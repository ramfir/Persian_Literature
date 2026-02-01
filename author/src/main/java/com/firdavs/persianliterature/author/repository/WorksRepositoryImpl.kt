package com.firdavs.persianliterature.author.repository

import android.content.Context
import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.firdavs.persianliterature.author.db.model.toDomain
import com.firdavs.persianliterature.author.model.WorkDTO
import com.firdavs.persianliterature.author.model.toDb
import com.firdavs.persianliterature.author_api.model.AudioCacheStatus
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.io.File

class WorksRepositoryImpl(
    private val worksDao: WorksDao,
    private val languageManager: LanguageManager,
    private val context: Context
) : WorksRepository {
    override suspend fun fetchWorks() {
        // Delete all downloaded audio files
        deleteDownloadedAudioFiles()

        // Delete all downloaded PDF files
        deleteDownloadedPdfFiles()

        // Clear database
        worksDao.deleteAll()

        // Fetch and insert new works
        val lang = languageManager.getSavedLanguage(context).firebaseCode
        val worksCollection = Firebase.firestore.collection("works_$lang")
        val snapshot = worksCollection.get(Source.SERVER).await()
        val worksDTO = snapshot.documents.mapNotNull { document ->
            val workDto = document.toObject(WorkDTO::class.java)
            workDto?.copy(id = document.id)
        }
        worksDao.insert(worksDTO.toDb())
    }

    private fun deleteDownloadedAudioFiles() {
        val audioDir = File(context.filesDir, "audio")
        if (audioDir.exists() && audioDir.isDirectory) {
            audioDir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }

    private fun deleteDownloadedPdfFiles() {
        context.filesDir.listFiles()?.forEach { file ->
            if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) {
                file.delete()
            }
        }
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

    override fun getWorksWithAudio(): Flow<List<Work>> {
        return worksDao.getWorksWithAudio().map { works ->
            works.toDomain()
        }
    }

    // New cache-related methods
    override suspend fun updateAudioCacheStatus(
        workId: String,
        cacheStatus: AudioCacheStatus,
        cachedBytes: Long,
        contentLength: Long
    ) {
        worksDao.updateAudioCacheStatus(workId, cacheStatus, cachedBytes, contentLength)
    }

    override fun getWorksWithFullyCachedAudio(): Flow<List<Work>> {
        return worksDao.getWorksWithFullyCachedAudio().map { works ->
            works.toDomain()
        }
    }
}
