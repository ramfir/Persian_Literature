package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.AudioCacheStatus
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.model.Work
import kotlinx.coroutines.flow.Flow

interface WorksRepository {
    suspend fun fetchWorks()
    fun getWorksByAuthorId(authorId: String): Flow<List<Work>>
    fun getWork(id: String): Flow<Work>
    suspend fun updateLastReadPage(workId: String, page: Int)

    // Audio cache management (new)
    suspend fun updateAudioCacheStatus(
        workId: String,
        cacheStatus: AudioCacheStatus,
        cachedBytes: Long = 0L,
        contentLength: Long = 0L
    )
    fun getWorksWithFullyCachedAudio(): Flow<List<Work>>

    // Audio download management (deprecated - kept for backward compatibility)
    @Deprecated("Use updateAudioCacheStatus instead")
    suspend fun updateAudioDownloadStatus(
        workId: String,
        status: AudioDownloadStatus,
        localPath: String? = null
    )
    fun getWorksWithDownloadedAudio(): Flow<List<Work>>
    fun getWorksWithAudio(): Flow<List<Work>>
}
