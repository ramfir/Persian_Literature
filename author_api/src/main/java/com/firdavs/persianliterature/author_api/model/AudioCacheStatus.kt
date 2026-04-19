package com.firdavs.persianliterature.author_api.model

/**
 * Represents the caching status of an audio file.
 *
 * Audio files are progressively cached as users stream them.
 * ExoPlayer automatically manages the caching process.
 */
enum class AudioCacheStatus {
    /**
     * Audio has never been played or cached.
     */
    NOT_CACHED,

    /**
     * Some chunks of the audio are cached.
     * User can play from cache and stream remaining parts.
     */
    PARTIALLY_CACHED,

    /**
     * Entire audio file is cached.
     * Ready for offline playback.
     */
    FULLY_CACHED
}
