package com.firdavs.persianliterature.audio.di

import com.firdavs.persianliterature.audio.api.service.AudioServiceController
import com.firdavs.persianliterature.audio.service.AudioServiceControllerImpl
import org.koin.dsl.module

val audioModule = module {
    single<AudioServiceController> { AudioServiceControllerImpl(get()) }
}
