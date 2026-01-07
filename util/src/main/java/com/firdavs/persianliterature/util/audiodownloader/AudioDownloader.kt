package com.firdavs.persianliterature.util.audiodownloader

import androidx.annotation.MainThread
import java.io.File

interface AudioDownloader {
    suspend fun downloadAudioFile(
        audioUrl: String,
        fileName: String,
        @MainThread
        onProgress: (Float) -> Unit = {},
        @MainThread
        doOnSuccess: (File) -> Unit
    )
}
