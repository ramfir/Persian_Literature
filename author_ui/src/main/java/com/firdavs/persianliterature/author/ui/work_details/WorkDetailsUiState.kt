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
    val audioErrorResId: Int? = null,
    val playbackState: PlaybackState = PlaybackState(),
    val currentlyPreparedAudioPath: String? = null,
    val hasCompletedInitialPreparation: Boolean = false,
    val showAudioControlToast: Boolean = false,
    val savedPage: Int = 0,

    // Text-reading mode
    val isTextMode: Boolean = false,
    val workText: WorkText? = null,
    val isLoadingText: Boolean = false,
    val textLoadFailed: Boolean = false,
    val fontSizeSp: Int = DEFAULT_FONT_SIZE_SP,
    val savedTextPosition: Int = 0
) : UiState() {
    val hasText: Boolean get() = work?.textUrl?.isNotBlank() == true

    companion object {
        const val DEFAULT_FONT_SIZE_SP = 18
        const val MIN_FONT_SIZE_SP = 12
        const val MAX_FONT_SIZE_SP = 34
        const val FONT_SIZE_STEP_SP = 2
    }
}
