package com.firdavs.persianliterature.audio.api.player

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for audio playback functionality
 */
interface AudioPlayer {
    /**
     * Observable playback state
     */
    val playbackState: StateFlow<PlaybackState>

    /**
     * Prepare and load audio from URL
     * @param url The audio file URL to load
     */
    fun prepare(url: String)

    /**
     * Start or resume playback
     */
    fun play()

    /**
     * Pause playback
     */
    fun pause()

    /**
     * Stop playback and release resources
     */
    fun stop()

    /**
     * Seek to a specific position in the audio
     * @param positionMs The position to seek to in milliseconds
     */
    fun seekTo(positionMs: Long)

    /**
     * Release all resources. Call when player is no longer needed
     */
    fun release()
}
