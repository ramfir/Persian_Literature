package com.firdavs.persianliterature.audio.di

import androidx.media3.common.util.UnstableApi
import com.firdavs.persianliterature.audio.api.service.AudioServiceController
import com.firdavs.persianliterature.audio.cache.AudioCacheManager
import com.firdavs.persianliterature.audio.service.AudioServiceControllerImpl
import org.koin.dsl.module

@UnstableApi
val audioModule = module {
    single { AudioCacheManager(get()) }
    single<AudioServiceController> { AudioServiceControllerImpl(get()) }
}
