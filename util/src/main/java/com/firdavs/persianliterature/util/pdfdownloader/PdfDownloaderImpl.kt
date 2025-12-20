package com.firdavs.persianliterature.util.pdfdownloader

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PdfDownloaderImpl(private val context: Context) : PdfDownloader {
    override suspend fun downloadPdfFile(
        pdfUrl: String,
        doOnSuccess: (File) -> Unit
    ) {
        val filePath = downloadToFile(pdfUrl)
        withContext(Dispatchers.Main) {
            doOnSuccess(File(filePath))
        }
    }

    private fun downloadToFile(pdfUrl: String): String {
        val filePath = context.filesDir.toString() + "/$downloadedFileName"

        val url = URL(pdfUrl)
        val connection = url.openConnection()
        connection.connect()

        // download the file
        BufferedInputStream(url.openStream(), bufferSize).use { input ->
            // Output stream
            FileOutputStream(filePath).use { output ->
                val data = input.readBytes()
                output.write(data)
                // flushing output
                output.flush()
            }
        }
        return filePath
    }

    companion object {
        private const val downloadedFileName: String = "downloaded.pdf"
        private const val bufferSize: Int = 8192
    }
}
