package com.firdavs.persianliterature.audio.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.firdavs.persianliterature.audio.R
import com.firdavs.persianliterature.audio.api.player.PlaybackState
import com.firdavs.persianliterature.audio.api.service.AudioServiceController
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Implementation of AudioServiceController that connects to AudioPlaybackService
 * via MediaController and exposes a simplified API to UI components.
 */
class AudioServiceControllerImpl(
    private val context: Context
) : AudioServiceController {

    private var controllerFuture: ListenableFuture<MediaController>? = null
    private var mediaController: MediaController? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var positionUpdateJob: Job? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            updatePlaybackState()

            if (playbackState == Player.STATE_READY) {
                startPositionUpdates()
            } else if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED) {
                stopPositionUpdates()
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
            val errorResId = when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                    R.string.audio_error_network
                PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED,
                PlaybackException.ERROR_CODE_PARSING_MANIFEST_MALFORMED ->
                    R.string.audio_error_format
                else ->
                    R.string.audio_error_playback
            }

            _playbackState.value = _playbackState.value.copy(
                errorResId = errorResId,
                isPlaying = false,
                isPreparing = false
            )
        }
    }

    override fun connect() {
        if (controllerFuture != null) return

        val sessionToken = SessionToken(
            context,
            ComponentName(context, AudioPlaybackService::class.java)
        )

        controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture?.addListener({
            mediaController = controllerFuture?.get()
            mediaController?.addListener(playerListener)
            updatePlaybackState()
        }, MoreExecutors.directExecutor())
    }

    override fun disconnect() {
        stopPositionUpdates()
        mediaController?.removeListener(playerListener)
        MediaController.releaseFuture(controllerFuture ?: return)
        mediaController = null
        controllerFuture = null
    }

    override fun syncPlaybackState() {
        if (playbackState.value.isPlaying) {
            startPositionUpdates()
        }
    }

    override fun prepareAudio(url: String, workTitle: String, authorName: String) {
        ensureServiceStarted()

        scope.launch {
            val controller = waitForController() ?: return@launch

            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(workTitle)
                .setArtist(authorName)
                .build()

            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .setMediaMetadata(mediaMetadata)
                .build()

            controller.setMediaItem(mediaItem)
            controller.prepare()

            _playbackState.value = _playbackState.value.copy(
                workTitle = workTitle,
                authorName = authorName,
                audioUrl = url,
                isPreparing = true,
                errorResId = null
            )
        }
    }

    override fun play() {
        ensureServiceStarted()
        mediaController?.play()
    }

    override fun pause() {
        mediaController?.pause()
    }

    override fun stop() {
        mediaController?.stop()
        stopService()
    }

    override fun seekTo(positionMs: Long) {
        mediaController?.seekTo(positionMs)
        updatePlaybackState()
    }

    override fun skipForward(durationMs: Long) {
        mediaController?.let {
            val newPosition = (it.currentPosition + durationMs).coerceAtMost(it.duration)
            it.seekTo(newPosition)
        }
    }

    override fun skipBackward(durationMs: Long) {
        mediaController?.let {
            val newPosition = (it.currentPosition - durationMs).coerceAtLeast(0)
            it.seekTo(newPosition)
        }
    }

    private fun ensureServiceStarted() {
        val intent = Intent(context, AudioPlaybackService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        if (controllerFuture == null) {
            connect()
        }
    }

    private fun stopService() {
        context.stopService(Intent(context, AudioPlaybackService::class.java))
    }

    @Suppress("MagicNumber")
    private suspend fun waitForController(): MediaController? {
        repeat(50) {
            mediaController?.let { return it }
            delay(100)
        }
        return null
    }

    private fun updatePlaybackState() {
        val controller = mediaController ?: return
        val metadata = controller.mediaMetadata

        // Clear error only when playback is successful (ready or playing)
        val shouldClearError = controller.playbackState == Player.STATE_READY ||
            controller.playbackState == Player.STATE_BUFFERING ||
            controller.isPlaying

        _playbackState.value = _playbackState.value.copy(
            isPlaying = controller.isPlaying,
            isPreparing = controller.playbackState == Player.STATE_BUFFERING,
            currentPosition = controller.currentPosition.coerceAtLeast(0),
            duration = controller.duration.coerceAtLeast(0),
            workTitle = metadata.title?.toString() ?: _playbackState.value.workTitle,
            authorName = metadata.artist?.toString() ?: _playbackState.value.authorName,
            audioUrl = _playbackState.value.audioUrl,
            errorResId = if (shouldClearError) null else _playbackState.value.errorResId
        )
    }

    @Suppress("MagicNumber")
    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = scope.launch {
            while (true) {
                updatePlaybackState()
                delay(500)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }
}
