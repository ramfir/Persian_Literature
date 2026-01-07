package com.firdavs.persianliterature.util.di

import com.firdavs.persianliterature.util.audiodownloader.AudioDownloader
import com.firdavs.persianliterature.util.audiodownloader.AudioDownloaderImpl
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloader
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloaderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val utilModule = module {
    factory { PdfDownloaderImpl(androidContext()) } bind PdfDownloader::class
    factory { AudioDownloaderImpl(androidContext()) } bind AudioDownloader::class
}
