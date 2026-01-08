package com.firdavs.persianliterature.audio.service

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.media3.common.Player
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.firdavs.persianliterature.audio.player.MediaSessionAudioPlayer

/**
 * Foreground service for audio playback with notification controls.
 * Extends MediaSessionService for automatic notification management.
 */
class AudioPlaybackService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var audioPlayer: MediaSessionAudioPlayer? = null

    override fun onCreate() {
        super.onCreate()
        initializeSessionAndPlayer()
    }

    private fun initializeSessionAndPlayer() {
        audioPlayer = MediaSessionAudioPlayer(this)

        mediaSession = MediaSession.Builder(this, audioPlayer!!.getPlayer())
            .setSessionActivity(createPendingIntent())
            .build()

        audioPlayer?.getPlayer()?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                val player = audioPlayer?.getPlayer()
                if (!isPlaying && player?.playbackState == Player.STATE_IDLE) {
                    stopSelf()
                }
            }
        })
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        return PendingIntent.getActivity(this, 0, intent, flags)
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = audioPlayer?.getPlayer()
        if (player?.isPlaying != true) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        audioPlayer?.release()
        audioPlayer = null
        super.onDestroy()
    }
}
