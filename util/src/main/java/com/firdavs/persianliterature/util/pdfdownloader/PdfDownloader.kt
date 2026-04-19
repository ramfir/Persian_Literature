package com.firdavs.persianliterature.util.pdfdownloader

import androidx.annotation.MainThread
import java.io.File

interface PdfDownloader {
    suspend fun downloadPdfFile(
        pdfUrl: String,
        fileName: String,
        @MainThread
        onProgress: (Float) -> Unit = {},
        @MainThread
        doOnSuccess: (File) -> Unit
    )
}
