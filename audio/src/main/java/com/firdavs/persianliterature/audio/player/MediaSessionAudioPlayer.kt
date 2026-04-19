package com.firdavs.persianliterature.audio.player

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.firdavs.persianliterature.audio.cache.AudioCacheManager
import java.io.File

/**
 * Wrapper around ExoPlayer that implements Player interface for MediaSession.
 * Handles audio focus and media item preparation with caching support.
 */
@UnstableApi
class MediaSessionAudioPlayer(
    context: Context,
    private val audioCacheManager: AudioCacheManager
) {

    private val exoPlayer: ExoPlayer

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
            .build()

        exoPlayer = ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setWakeMode(C.WAKE_MODE_LOCAL)
            .setMediaSourceFactory(
                DefaultMediaSourceFactory(context)
                    .setDataSourceFactory(audioCacheManager.getCacheDataSourceFactory(context))
            )
            .build()
    }

    /**
     * Returns the underlying ExoPlayer instance for MediaSession
     */
    fun getPlayer(): Player = exoPlayer

    /**
     * Prepare audio with metadata for notification display
     */
    fun prepareAudio(url: String, workTitle: String, authorName: String) {
        val uri = if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            val file = File(url)
            if (file.exists()) {
                file.toURI().toString()
            } else {
                return
            }
        }

        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(workTitle)
            .setArtist(authorName)
            .setAlbumTitle("Persian Literature")
            .build()

        val mediaItem = MediaItem.Builder()
            .setUri(uri)
            .setMediaMetadata(mediaMetadata)
            .build()

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    /**
     * Release the player resources
     */
    fun release() {
        exoPlayer.release()
    }
}
