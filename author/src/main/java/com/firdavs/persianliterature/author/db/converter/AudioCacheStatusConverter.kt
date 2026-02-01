package com.firdavs.persianliterature.author.db.converter

import androidx.room.TypeConverter
import com.firdavs.persianliterature.author_api.model.AudioCacheStatus

/**
 * Room TypeConverter for AudioCacheStatus enum.
 * Converts between enum and String for database storage.
 */
class AudioCacheStatusConverter {

    @TypeConverter
    fun fromAudioCacheStatus(status: AudioCacheStatus): String {
        return status.name
    }

    @TypeConverter
    fun toAudioCacheStatus(value: String): AudioCacheStatus {
        return try {
            AudioCacheStatus.valueOf(value)
        } catch (e: IllegalArgumentException) {
            // Fallback to NOT_CACHED if value is invalid
            AudioCacheStatus.NOT_CACHED
        }
    }
}
