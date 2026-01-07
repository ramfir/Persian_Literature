package com.firdavs.persianliterature.audio.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.firdavs.persianliterature.audio.api.player.AudioPlayer
import com.firdavs.persianliterature.audio.api.player.PlaybackState
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Implementation of AudioPlayer using ExoPlayer
 */
class ExoPlayerAudioPlayer(
    private val context: Context
) : AudioPlayer {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var positionUpdateJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE -> {
                    updatePlaybackState()
                }
                Player.STATE_BUFFERING -> {
                    _playbackState.value = _playbackState.value.copy(isPreparing = true)
                }
                Player.STATE_READY -> {
                    updatePlaybackState()
                    _playbackState.value = _playbackState.value.copy(isPreparing = false)
                }
                Player.STATE_ENDED -> {
                    stopPositionUpdates()
                    updatePlaybackState()
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (isPlaying) {
                startPositionUpdates()
            } else {
                stopPositionUpdates()
            }
            updatePlaybackState()
        }

        override fun onPlayerError(error: PlaybackException) {
            stopPositionUpdates()
            _playbackState.value = PlaybackState(
                isPlaying = false,
                error = error.message ?: "Unknown error occurred"
            )
        }
    }

    private fun updatePlaybackState() {
        val player = exoPlayer ?: return
        _playbackState.value = _playbackState.value.copy(
            isPlaying = player.isPlaying,
            currentPosition = player.currentPosition.coerceAtLeast(0),
            duration = player.duration.coerceAtLeast(0)
        )
    }

    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = scope.launch {
            while (true) {
                updatePlaybackState()
                delay(500) // Update position every 500ms
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    override fun prepare(url: String) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(playerListener)
            }
        }

        // Support both URLs and local file paths
        val mediaItem = if (url.startsWith("http://") || url.startsWith("https://")) {
            MediaItem.fromUri(url)
        } else {
            // Local file path - ensure it exists
            val file = File(url)
            if (file.exists()) {
                MediaItem.fromUri(file.toURI().toString())
            } else {
                _playbackState.value = PlaybackState(
                    error = "Audio file not found. Please download it first."
                )
                return
            }
        }

        exoPlayer?.apply {
            setMediaItem(mediaItem)
            prepare()
        }

        _playbackState.value = PlaybackState(isPreparing = true)
    }

    override fun play() {
        exoPlayer?.play()
    }

    override fun pause() {
        exoPlayer?.pause()
    }

    override fun stop() {
        stopPositionUpdates()
        exoPlayer?.stop()
        _playbackState.value = PlaybackState(isPlaying = false)
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        updatePlaybackState()
    }

    override fun release() {
        stopPositionUpdates()
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
        _playbackState.value = PlaybackState()
    }
}
