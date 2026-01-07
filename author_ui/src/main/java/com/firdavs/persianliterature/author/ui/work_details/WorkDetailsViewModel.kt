package com.firdavs.persianliterature.author.ui.work_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.audio.api.player.AudioPlayer
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
    private val audioPlayer: AudioPlayer,
    private val favouritesRepository: FavouritesRepository
) : BaseViewModel<WorkDetailsUiState>(WorkDetailsUiState(null)) {
    private val downloadPdfScope = CoroutineScope(Job() + Dispatchers.IO)
    private val downloadAudioScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        observeWork()
        observeAudioPlayback()
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
                                    audioPlayer.prepare(localPath)
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
            audioPlayer.playbackState.collect { playbackState ->
                post {
                    it.copy(
                        playbackState = playbackState,
                        audioDownloadError = playbackState.error
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
                    audioPlayer.prepare(audioFile.absolutePath)
                }
            }
        )
    }

    // Play audio
    fun onPlayAudio() {
        val audioFile = state.value.audioFile
        if (audioFile != null && audioFile.exists()) {
            audioPlayer.play()
        } else {
            post { it.copy(audioDownloadError = "Audio not downloaded") }
        }
    }

    // Pause audio
    fun onPauseAudio() {
        audioPlayer.pause()
    }

    // Stop audio
    fun onStopAudio() {
        audioPlayer.stop()
    }

    // Seek to position
    fun onSeekTo(positionMs: Long) {
        audioPlayer.seekTo(positionMs)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
