package com.firdavs.persianliterature.app.di

import com.firdavs.persianliterature.about_app.di.aboutAppUiModule
import com.firdavs.persianliterature.app.ui.MainViewModel
import com.firdavs.persianliterature.audio.di.audioModule
import com.firdavs.persianliterature.author.di.authorModule
import com.firdavs.persianliterature.author.ui.di.authorUiModule
import com.firdavs.persianliterature.settings.DailyNotificationWorker
import com.firdavs.persianliterature.settings.LanguageManagerImpl
import com.firdavs.persianliterature.settings.LanguageViewModel
import com.firdavs.persianliterature.settings.NotificationManagerImpl
import com.firdavs.persianliterature.settings.SettingsViewModel
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.settings.api.NotificationManager
import com.firdavs.persianliterature.util.di.utilModule
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LanguageViewModel)
    singleOf(::LanguageManagerImpl) bind LanguageManager::class
    singleOf(::NotificationManagerImpl) bind NotificationManager::class

    workerOf(::DailyNotificationWorker)

    includes(
        authorUiModule,
        authorModule,
        utilModule,
        aboutAppUiModule,
        audioModule
    )
}
