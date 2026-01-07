package com.firdavs.persianliterature.audio.di

import com.firdavs.persianliterature.audio.api.player.AudioPlayer
import com.firdavs.persianliterature.audio.player.ExoPlayerAudioPlayer
import org.koin.dsl.module

val audioModule = module {
    single<AudioPlayer> { ExoPlayerAudioPlayer(get()) }
}
