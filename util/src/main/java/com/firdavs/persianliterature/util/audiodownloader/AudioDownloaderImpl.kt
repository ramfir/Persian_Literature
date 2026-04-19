package com.firdavs.persianliterature.util.audiodownloader

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class AudioDownloaderImpl(private val context: Context) : AudioDownloader {
    override suspend fun downloadAudioFile(
        audioUrl: String,
        fileName: String,
        onProgress: (Float) -> Unit,
        doOnSuccess: (File) -> Unit
    ) {
        val filePath = downloadToFile(audioUrl, fileName, onProgress)
        withContext(Dispatchers.Main) {
            doOnSuccess(File(filePath))
        }
    }

    @Suppress("NestedBlockDepth")
    private fun downloadToFile(
        audioUrl: String,
        downloadedFileName: String,
        onProgress: (Float) -> Unit
    ): String {
        // Store audio files in subdirectory for organization
        val audioDir = File(context.filesDir, "audio")
        if (!audioDir.exists()) {
            audioDir.mkdirs()
        }
        val filePath = "${audioDir.absolutePath}/$downloadedFileName.mp3"

        val url = URL(audioUrl)
        val connection = url.openConnection()
        connection.connect()

        val fileLength = connection.contentLength

        // Download with progress tracking
        BufferedInputStream(url.openStream(), bufferSize).use { input ->
            FileOutputStream(filePath).use { output ->
                val data = ByteArray(bufferSize)
                var total = 0L
                var count: Int

                while (input.read(data).also { count = it } != -1) {
                    total += count
                    output.write(data, 0, count)

                    // Report progress
                    if (fileLength > 0) {
                        val progress = total.toFloat() / fileLength.toFloat()
                        onProgress(progress)
                    }
                }
                output.flush()
            }
        }
        return filePath
    }

    companion object {
        private const val bufferSize: Int = 8192
    }
}
