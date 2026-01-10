package com.firdavs.persianliterature.audio.api.player

/**
 * Represents the current state of audio playback
 */
data class PlaybackState(
    val isPlaying: Boolean = false,
    val isPreparing: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val error: String? = null,
    val workTitle: String? = null,
    val authorName: String? = null,
    val audioUrl: String? = null
) {
    /**
     * Returns the progress as a fraction between 0.0 and 1.0
     */
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration.toFloat() else 0f
}
