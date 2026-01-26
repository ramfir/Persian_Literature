package com.firdavs.persianliterature.author.ui.work_details

import com.firdavs.persianliterature.audio.api.player.PlaybackState
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.presentation.UiState
import java.io.File

data class WorkDetailsUiState(
    val id: String?,
    val work: Work? = null,
    val workFile: File? = null,
    val isDownloadingPdf: Boolean = false,
    val pdfDownloadProgress: Float = 0f,
    val audioFile: File? = null,
    val isDownloadingAudio: Boolean = false,
    val audioDownloadProgress: Float = 0f,
    val audioDownloadError: String? = null,
    val playbackState: PlaybackState = PlaybackState(),
    val currentlyPreparedAudioPath: String? = null,
    val hasCompletedInitialPreparation: Boolean = false,
    val showAudioControlToast: Boolean = false
) : UiState()
