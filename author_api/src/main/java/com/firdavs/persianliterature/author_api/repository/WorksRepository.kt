package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Work
import kotlinx.coroutines.flow.Flow

interface WorksRepository {
    suspend fun fetchWorks()
    fun getWorksByAuthorId(authorId: String): Flow<List<Work>>
    fun getWork(id: String): Flow<Work>

    // Audio download management
    suspend fun updateAudioDownloadStatus(
        workId: String,
        status: AudioDownloadStatus,
        localPath: String? = null
    )
    fun getWorksWithDownloadedAudio(): Flow<List<Work>>
    fun getWorksWithAudio(): Flow<List<Work>>
}
