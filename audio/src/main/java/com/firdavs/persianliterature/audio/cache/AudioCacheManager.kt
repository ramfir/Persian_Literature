package com.firdavs.persianliterature.audio.cache

import android.content.Context
import android.net.Uri
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSink
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

/**
 * Manages ExoPlayer cache for audio streaming and offline playback.
 *
 * Uses SimpleCache with LRU eviction strategy to store streamed audio chunks.
 * Cached content is available for offline playback.
 */
@UnstableApi
@Suppress("MagicNumber")
class AudioCacheManager(context: Context) {

    private val cacheDir = File(context.cacheDir, "exoplayer_audio")
    private val databaseProvider = StandaloneDatabaseProvider(context)

    // Cache size: 500MB for audio files
    private val maxCacheSize: Long = 500 * 1024 * 1024L

    /**
     * The SimpleCache instance. Lazily initialized.
     * Uses LRU eviction when cache size exceeds maxCacheSize.
     */
    val cache: SimpleCache by lazy {
        SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(maxCacheSize),
            databaseProvider
        )
    }

    /**
     * Creates a CacheDataSource.Factory for ExoPlayer.
     * This factory produces data sources that read from cache first,
     * then from network if not cached, while writing to cache.
     */
    fun getCacheDataSourceFactory(context: Context): DataSource.Factory {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setConnectTimeoutMs(DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS)
            .setReadTimeoutMs(DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS)

        return CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(
                CacheDataSink.Factory()
                    .setCache(cache)
                    .setFragmentSize(CacheDataSink.DEFAULT_FRAGMENT_SIZE)
            )
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    /**
     * Get the total cached bytes for a specific URL.
     *
     * @param uri The audio file URI
     * @return Total bytes cached for this URI
     */
    fun getCachedBytes(uri: Uri): Long {
        val cachedSpans = cache.getCachedSpans(uri.toString())
        return cachedSpans.sumOf { it.length }
    }

    /**
     * Get the total content length for a specific URL from cache metadata.
     *
     * @param uri The audio file URI
     * @return Total content length in bytes, or -1 if not available yet
     */
    fun getContentLength(uri: Uri): Long {
        return try {
            val contentMetadata = cache.getContentMetadata(uri.toString())
            androidx.media3.datasource.cache.ContentMetadata.getContentLength(contentMetadata)
        } catch (e: Exception) {
            -1L
        }
    }

    /**
     * Check if the entire file is fully cached.
     *
     * @param uri The audio file URI
     * @param contentLength The total content length of the file
     * @return true if fully cached, false otherwise
     */
    fun isFullyCached(uri: Uri, contentLength: Long): Boolean {
        if (contentLength <= 0) return false
        return getCachedBytes(uri) >= contentLength
    }

    /**
     * Remove a specific resource from the cache.
     *
     * @param uri The audio file URI to remove
     */
    fun removeFromCache(uri: Uri) {
        try {
            cache.removeResource(uri.toString())
        } catch (e: Exception) {
            // Log error if needed
        }
    }

    /**
     * Release the cache resources.
     * Should be called when the cache is no longer needed.
     */
    fun release() {
        try {
            cache.release()
        } catch (e: Exception) {
            // Log error if needed
        }
    }
}
