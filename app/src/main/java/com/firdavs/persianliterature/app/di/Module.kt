package com.firdavs.persianliterature.app.di

import com.firdavs.persianliterature.about_app.di.aboutAppUiModule
import com.firdavs.persianliterature.app.ui.MainViewModel
import com.firdavs.persianliterature.author.di.authorModule
import com.firdavs.persianliterature.author.ui.di.authorUiModule
import com.firdavs.persianliterature.util.di.utilModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)

    includes(
        authorUiModule,
        authorModule,
        utilModule,
        aboutAppUiModule
    )
}
