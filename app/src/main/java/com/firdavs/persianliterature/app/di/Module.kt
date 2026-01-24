package com.firdavs.persianliterature.app.di

import com.firdavs.persianliterature.about_app.di.aboutAppUiModule
import com.firdavs.persianliterature.app.ui.MainViewModel
import com.firdavs.persianliterature.audio.di.audioModule
import com.firdavs.persianliterature.author.di.authorModule
import com.firdavs.persianliterature.author.ui.di.authorUiModule
import com.firdavs.persianliterature.poem_of_day.di.poemOfDayModule
import com.firdavs.persianliterature.quiz.di.quizModule
import com.firdavs.persianliterature.quiz.ui.di.quizUiModule
import com.firdavs.persianliterature.settings.data.LanguageManagerImpl
import com.firdavs.persianliterature.settings.data.NotificationManagerImpl
import com.firdavs.persianliterature.settings.ui.language.LanguageViewModel
import com.firdavs.persianliterature.settings.ui.main.SettingsViewModel
import com.firdavs.persianliterature.settings.worker.DailyNotificationWorker
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.settings.api.LocaleHolder
import com.firdavs.persianliterature.settings.api.NotificationManager
import com.firdavs.persianliterature.util.di.utilModule
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::LanguageViewModel)
    single { LanguageManagerImpl(get()) } binds arrayOf(LanguageManager::class, LocaleHolder::class)
    singleOf(::NotificationManagerImpl) bind NotificationManager::class

    workerOf(::DailyNotificationWorker)

    includes(
        authorUiModule,
        authorModule,
        utilModule,
        aboutAppUiModule,
        audioModule,
        quizModule,
        quizUiModule,
        poemOfDayModule
    )
}
