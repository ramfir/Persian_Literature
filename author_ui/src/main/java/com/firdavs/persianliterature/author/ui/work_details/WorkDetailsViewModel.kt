package com.firdavs.persianliterature.author.ui.work_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.audio.api.player.PlaybackState
import com.firdavs.persianliterature.audio.api.service.AudioServiceController
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.util.audiodownloader.AudioDownloader
import com.firdavs.persianliterature.util.coroutines.runWithRetry
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

@SuppressLint("StaticFieldLeak")
class WorkDetailsViewModel(
    private val id: String,
    private val context: Context,
    private val worksRepository: WorksRepository,
    private val pdfDownloader: PdfDownloader,
    private val audioDownloader: AudioDownloader,
    private val audioServiceController: AudioServiceController,
    private val favouritesRepository: FavouritesRepository
) : BaseViewModel<WorkDetailsUiState>(WorkDetailsUiState(null)) {
    private val downloadPdfScope = CoroutineScope(Job() + Dispatchers.IO)
    private val downloadAudioScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        audioServiceController.connect()
        observeWork()
        observeAudioPlayback()
    }

    override fun onViewResumed() {
        super.onViewResumed()
        // Force immediate state sync when returning to screen
        syncPlaybackState()
    }

    private fun syncPlaybackState() {
        viewModelScope.launch {
            val currentPlaybackState = audioServiceController.playbackState.value
            val currentAudioPath = state.value.audioFile?.absolutePath
            val expectedAudioPath = state.value.work?.audioLocalPath

            // Check if this work's audio is currently playing
            val isThisWorkPlaying = when {
                currentPlaybackState.audioUrl == null -> false
                currentAudioPath != null && currentPlaybackState.audioUrl == currentAudioPath -> true
                expectedAudioPath != null && currentPlaybackState.audioUrl == expectedAudioPath -> true
                else -> false
            }

            // Update state with current playback position
            if (isThisWorkPlaying) {
                post { it.copy(playbackState = currentPlaybackState) }
            }
        }
    }

    private fun observeWork() {
        viewModelScope.launch {
            worksRepository.getWork(id).collect { work ->
                post {
                    it.copy(work = work)
                }
                // Handle PDF download (existing logic)
                work.fileUrl?.let {
                    val workFilePath = context.filesDir.toString() + "/${work.id}"
                    val workFile = File(workFilePath)
                    if (workFile.exists().not()) {
                        downloadPdf(it, work.id)
                    } else {
                        post { it.copy(workFile = workFile, isLoading = false) }
                    }
                } ?: post { it.copy(isLoading = false) }

                // Handle audio file checking
                work.audioUrl?.let { audioUrl ->
                    when (work.audioDownloadStatus) {
                        AudioDownloadStatus.DOWNLOADED -> {
                            work.audioLocalPath?.let { localPath ->
                                val audioFile = File(localPath)
                                if (audioFile.exists()) {
                                    post { it.copy(audioFile = audioFile) }
                                } else {
                                    // File deleted externally, update status
                                    worksRepository.updateAudioDownloadStatus(
                                        work.id,
                                        AudioDownloadStatus.NOT_DOWNLOADED,
                                        null
                                    )
                                }
                            }
                        }
                        AudioDownloadStatus.DOWNLOADING -> {
                            post { it.copy(isDownloadingAudio = true) }
                        }
                        else -> {
                            // NOT_DOWNLOADED or FAILED - no action
                        }
                    }
                }
            }
        }
    }

    private fun downloadPdf(url: String, fileName: String) {
        downloadPdfScope.launch {
            post { it.copy(isLoading = true) }
            runWithRetry(maxAttempts = 5) { tryDownloadPdf(url, fileName) }?.let {
                post { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun tryDownloadPdf(url: String, fileName: String) {
        pdfDownloader.downloadPdfFile(pdfUrl = url, fileName = fileName) { pdfFile ->
            post { it.copy(isLoading = false) }
            post { it.copy(workFile = pdfFile) }
        }
    }

    fun onToggleFavourite(isFavourite: Boolean) {
        viewModelScope.launch {
            favouritesRepository.toggleWorkFavourite(id, isFavourite)
        }
    }

    // Audio playback observation
    private fun observeAudioPlayback() {
        viewModelScope.launch {
            audioServiceController.playbackState.collect { playbackState ->
                post {
                    // Check if this work's audio is playing by comparing audio paths
                    // We need to check both the current audio file and any expected audio path
                    val currentAudioPath = it.audioFile?.absolutePath
                    val expectedAudioPath = it.work?.audioLocalPath

                    // Consider it "this work" if the playback URL matches either path
                    val isThisWorkPlaying = when {
                        playbackState.audioUrl == null -> false
                        currentAudioPath != null && playbackState.audioUrl == currentAudioPath -> true
                        expectedAudioPath != null && playbackState.audioUrl == expectedAudioPath -> true
                        else -> false
                    }

                    // Track if we've completed initial preparation
                    val hasCompletedPreparation = if (isThisWorkPlaying &&
                        !playbackState.isPreparing &&
                        playbackState.duration > 0) {
                        true
                    } else if (!isThisWorkPlaying) {
                        false // Reset when switching to different audio
                    } else {
                        it.hasCompletedInitialPreparation
                    }

                    println("mmmm collect playbackState=$playbackState")
                    it.copy(
                        playbackState = if (isThisWorkPlaying || playbackState.audioUrl == null) {
                            // Show playback state if it's this work OR no audio is playing
                            playbackState
                        } else {
                            // Reset to default state if a different work is playing
                            PlaybackState()
                        },
                        audioDownloadError = if (isThisWorkPlaying) {
                            playbackState.error
                        } else {
                            it.audioDownloadError
                        },
                        // Update the currently prepared path when we receive playback state
                        currentlyPreparedAudioPath = if (isThisWorkPlaying) {
                            playbackState.audioUrl
                        } else {
                            it.currentlyPreparedAudioPath
                        },
                        hasCompletedInitialPreparation = hasCompletedPreparation
                    )
                }
            }
        }
    }

    // Download audio
    fun onDownloadAudio() {
        val work = state.value.work ?: return
        val audioUrl = work.audioUrl ?: return

        // Check if already downloaded
        if (work.audioDownloadStatus == AudioDownloadStatus.DOWNLOADED) {
            return
        }

        downloadAudioScope.launch {
            post {
                it.copy(
                    isDownloadingAudio = true,
                    audioDownloadProgress = 0f,
                    audioDownloadError = null
                )
            }

            // Update database status to DOWNLOADING
            worksRepository.updateAudioDownloadStatus(
                work.id,
                AudioDownloadStatus.DOWNLOADING
            )

            runWithRetry(maxAttempts = 5) {
                tryDownloadAudio(audioUrl, work.title, work.id)
            }?.let { error ->
                // Download failed after retries
                post {
                    it.copy(
                        isDownloadingAudio = false,
                        audioDownloadError = "Download failed: ${error.message}"
                    )
                }
                worksRepository.updateAudioDownloadStatus(
                    work.id,
                    AudioDownloadStatus.FAILED
                )
            }
        }
    }

    private suspend fun tryDownloadAudio(url: String, fileName: String, workId: String) {
        audioDownloader.downloadAudioFile(
            audioUrl = url,
            fileName = fileName,
            onProgress = { progress ->
                post { it.copy(audioDownloadProgress = progress) }
            },
            doOnSuccess = { audioFile ->
                viewModelScope.launch {
                    worksRepository.updateAudioDownloadStatus(
                        workId,
                        AudioDownloadStatus.DOWNLOADED,
                        audioFile.absolutePath
                    )
                    post {
                        it.copy(
                            audioFile = audioFile,
                            isDownloadingAudio = false,
                            audioDownloadProgress = 1f
                        )
                    }
                }
            }
        )
    }

    // Play audio
    fun onPlayAudio() {
        val audioFile = state.value.audioFile
        val work = state.value.work
        val currentPlaybackState = state.value.playbackState

        if (audioFile != null && audioFile.exists() && work != null) {
            // Check if the service already has this audio prepared
            val isAlreadyPrepared = currentPlaybackState.audioUrl == audioFile.absolutePath

            // Only prepare if this is a different audio file or nothing is prepared yet
            if (!isAlreadyPrepared) {
                audioServiceController.prepareAudio(
                    audioFile.absolutePath,
                    work.title,
                    "Persian Literature"
                )
                post { it.copy(currentlyPreparedAudioPath = audioFile.absolutePath) }
            }
            audioServiceController.play()
        } else {
            post { it.copy(audioDownloadError = "Audio not downloaded") }
        }
    }

    // Pause audio
    fun onPauseAudio() {
        audioServiceController.pause()
    }

    // Seek to position
    fun onSeekTo(positionMs: Long) {
        audioServiceController.seekTo(positionMs)
    }

    // Skip 10 seconds backward
    fun onSkipBackward() {
        audioServiceController.skipBackward(SKIP_DURATION_MS)
    }

    // Skip 10 seconds forward
    fun onSkipForward() {
        audioServiceController.skipForward(SKIP_DURATION_MS)
    }

    override fun onCleared() {
        super.onCleared()
        audioServiceController.disconnect()
    }

    companion object {
        private const val SKIP_DURATION_MS = 10000L
    }
}
