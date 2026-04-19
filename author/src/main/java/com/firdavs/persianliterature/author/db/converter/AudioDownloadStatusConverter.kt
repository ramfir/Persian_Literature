package com.firdavs.persianliterature.author.db.converter

import androidx.room.TypeConverter
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus

class AudioDownloadStatusConverter {
    @TypeConverter
    fun fromAudioDownloadStatus(status: AudioDownloadStatus): String {
        return status.name
    }

    @TypeConverter
    fun toAudioDownloadStatus(value: String): AudioDownloadStatus {
        return try {
            AudioDownloadStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            AudioDownloadStatus.NOT_DOWNLOADED
        }
    }
}
