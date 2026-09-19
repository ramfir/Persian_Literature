package com.firdavs.persianliterature.author.ui.work_details

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.audio.api.player.PlaybackState
import com.firdavs.persianliterature.audio.api.service.AudioServiceController
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.util.coroutines.runWithRetry
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File

@SuppressLint("StaticFieldLeak")
class WorkDetailsViewModel(
    private val id: String,
    private val context: Context,
    private val worksRepository: WorksRepository,
    private val pdfDownloader: PdfDownloader,
    private val audioServiceController: AudioServiceController,
    private val favouritesRepository: FavouritesRepository,
    private val languageManager: LanguageManager
) : BaseViewModel<WorkDetailsUiState>(WorkDetailsUiState(null)) {
    private val downloadPdfScope = CoroutineScope(Job() + Dispatchers.IO)
    private var firstAudioPlay = true

    private val readerPrefs by lazy {
        context.getSharedPreferences(READER_PREFS, Context.MODE_PRIVATE)
    }

    init {
        audioServiceController.connect()
        post { it.copy(fontSizeSp = readerPrefs.getInt(KEY_FONT_SIZE, WorkDetailsUiState.DEFAULT_FONT_SIZE_SP)) }
        observeWork()
        observeAudioPlayback()
    }

    fun onToggleReadMode() {
        val current = state.value
        val goingToText = !current.isTextMode
        post { it.copy(isTextMode = goingToText) }
        if (goingToText && current.workText == null && !current.isLoadingText) {
            loadWorkText()
        }
    }

    fun onIncreaseFont() = changeFontSize(WorkDetailsUiState.FONT_SIZE_STEP_SP)

    fun onDecreaseFont() = changeFontSize(-WorkDetailsUiState.FONT_SIZE_STEP_SP)

    private fun changeFontSize(delta: Int) {
        val newSize = (state.value.fontSizeSp + delta)
            .coerceIn(WorkDetailsUiState.MIN_FONT_SIZE_SP, WorkDetailsUiState.MAX_FONT_SIZE_SP)
        if (newSize == state.value.fontSizeSp) return
        readerPrefs.edit().putInt(KEY_FONT_SIZE, newSize).apply()
        post { it.copy(fontSizeSp = newSize) }
    }

    fun retryLoadText() = loadWorkText()

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    private fun loadWorkText() {
        val work = state.value.work ?: return
        val textUrl = work.textUrl?.takeIf { it.isNotBlank() } ?: return
        downloadPdfScope.launch {
            post { it.copy(isLoadingText = true, textLoadFailed = false) }
            val languageCode = languageManager.getSavedLanguage(context).firebaseCode
            val fileName = "${work.id}_${languageCode}_text.json"
            val cachedFile = File(context.filesDir, fileName)
            try {
                if (cachedFile.exists().not()) {
                    pdfDownloader.downloadPdfFile(
                        pdfUrl = textUrl,
                        fileName = fileName,
                        doOnSuccess = {}
                    )
                }
                val parsed = WorkText.fromFile(File(context.filesDir, fileName))
                post { it.copy(workText = parsed, isLoadingText = false) }
            } catch (e: Exception) {
                if (cachedFile.exists()) cachedFile.delete()
                post { it.copy(isLoadingText = false, textLoadFailed = true) }
            }
        }
    }

    override fun onViewResumed() {
        // Sync playback state when screen becomes visible
        // This ensures the slider position is updated immediately
        audioServiceController.syncPlaybackState()
    }

    fun onPageChanged(page: Int) {
        post { it.copy(savedPage = page) }
    }

    fun onTextScrollPositionChanged(position: Int) {
        post { it.copy(savedTextPosition = position) }
    }

    fun persistReadingProgress() {
        val page = state.value.savedPage
        val textPosition = state.value.savedTextPosition
        runBlocking(Dispatchers.IO) {
            worksRepository.updateLastReadPage(id, page)
            worksRepository.updateLastReadTextPosition(id, textPosition)
        }
    }

    private fun observeWork() {
        viewModelScope.launch {
            worksRepository.getWork(id).collect { work ->
                post {
                    it.copy(
                        work = work,
                        savedPage = work.lastReadPage,
                        savedTextPosition = work.lastReadTextPosition
                    )
                }
                // Handle PDF download (existing logic)
                work.fileUrl?.let {
                    val languageCode = languageManager.getSavedLanguage(context).firebaseCode
                    val fileName = "${work.id}_$languageCode"
                    val workFilePath = context.filesDir.toString() + "/$fileName"
                    val workFile = File(workFilePath)
                    if (workFile.exists().not()) {
                        downloadPdf(it, fileName)
                    } else {
                        post { it.copy(workFile = workFile, isDownloadingPdf = false) }
                    }
                } ?: post { it.copy(isDownloadingPdf = false) }

                // Audio is now handled by ExoPlayer cache - no file checking needed
            }
        }
    }

    private fun downloadPdf(url: String, fileName: String) {
        downloadPdfScope.launch {
            // Clean up any leftover temporary file from previous failed attempts
            val tempFile = File(context.filesDir, "$fileName.tmp")
            if (tempFile.exists()) {
                tempFile.delete()
            }

            post {
                it.copy(
                    isDownloadingPdf = true,
                    pdfDownloadProgress = 0f
                )
            }
            runWithRetry(maxAttempts = 5) { tryDownloadPdf(url, fileName) }?.let {
                post {
                    it.copy(
                        isDownloadingPdf = false
                    )
                }
            }
        }
    }

    private suspend fun tryDownloadPdf(url: String, fileName: String) {
        pdfDownloader.downloadPdfFile(
            pdfUrl = url,
            fileName = fileName,
            onProgress = { progress ->
                post { it.copy(pdfDownloadProgress = progress) }
            },
            doOnSuccess = { pdfFile ->
                post {
                    it.copy(
                        isDownloadingPdf = false,
                        workFile = pdfFile,
                        pdfDownloadProgress = 1f
                    )
                }
            }
        )
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
                    // Check if this work's audio is playing by comparing audio URLs
                    val thisWorkAudioUrl = it.work?.audioUrl

                    // Consider it "this work" if the playback URL matches this work's audio URL
                    val isThisWorkPlaying = when {
                        playbackState.audioUrl == null -> false
                        thisWorkAudioUrl != null && playbackState.audioUrl == thisWorkAudioUrl -> true
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

                    it.copy(
                        playbackState = if (isThisWorkPlaying || playbackState.audioUrl == null) {
                            // Show playback state if it's this work OR no audio is playing
                            playbackState
                        } else {
                            // Reset to default state if a different work is playing
                            PlaybackState()
                        },
                        audioErrorResId = if (isThisWorkPlaying) {
                            playbackState.errorResId
                        } else {
                            it.audioErrorResId
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

    // Play audio - streams directly from URL
    fun onPlayAudio() {
        val work = state.value.work ?: return
        val audioUrl = work.audioUrl ?: return
        val currentPlaybackState = state.value.playbackState

        // Check if this is the first time playing audio
        if (firstAudioPlay) {
            firstAudioPlay = false
            post { it.copy(showAudioControlToast = true) }
        }

        // Check if the service already has this audio prepared
        val isAlreadyPrepared = currentPlaybackState.audioUrl == audioUrl

        // Only prepare if this is a different audio or nothing is prepared yet
        if (!isAlreadyPrepared) {
            audioServiceController.prepareAudio(
                audioUrl,
                work.title,
                "Persian Literature"
            )
            post { it.copy(currentlyPreparedAudioPath = audioUrl) }
        }
        audioServiceController.play()
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

    fun resetAudioControlToastFlag() {
        post { it.copy(showAudioControlToast = false) }
    }

    fun clearAudioError() {
        post { it.copy(audioErrorResId = null) }
    }

    override fun onCleared() {
        super.onCleared()
        audioServiceController.disconnect()
    }

    companion object {
        private const val SKIP_DURATION_MS = 10000L
        private const val READER_PREFS = "reader_preferences"
        private const val KEY_FONT_SIZE = "reader_font_size_sp"
    }
}
