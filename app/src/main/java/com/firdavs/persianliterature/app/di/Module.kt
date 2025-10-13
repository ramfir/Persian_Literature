package com.firdavs.persianliterature.app.di

import com.firdavs.persianliterature.app.ui.MainViewModel
import com.firdavs.persianliterature.author_ui.di.authorUiModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainViewModel)

    includes(
        authorUiModule
    )
}
