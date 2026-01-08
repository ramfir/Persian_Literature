package com.firdavs.persianliterature.audio.api.service

import com.firdavs.persianliterature.audio.api.player.PlaybackState
import kotlinx.coroutines.flow.StateFlow

/**
 * Controller for audio playback service.
 * This interface abstracts the service connection and provides
 * a simple API for UI components to control playback.
 */
interface AudioServiceController {
    /**
     * Observable playback state from the service
     */
    val playbackState: StateFlow<PlaybackState>

    /**
     * Prepare and load audio with metadata
     */
    fun prepareAudio(url: String, workTitle: String, authorName: String)

    /**
     * Start or resume playback
     */
    fun play()

    /**
     * Pause playback
     */
    fun pause()

    /**
     * Stop playback and dismiss notification
     */
    fun stop()

    /**
     * Seek to position in milliseconds
     */
    fun seekTo(positionMs: Long)

    /**
     * Skip forward by duration
     */
    fun skipForward(durationMs: Long = 10000L)

    /**
     * Skip backward by duration
     */
    fun skipBackward(durationMs: Long = 10000L)

    /**
     * Connect to service (called by ViewModel)
     */
    fun connect()

    /**
     * Disconnect from service (called when no longer needed)
     */
    fun disconnect()
}
