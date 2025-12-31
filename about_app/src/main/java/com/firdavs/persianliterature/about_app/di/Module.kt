package com.firdavs.persianliterature.about_app.di

import com.firdavs.persianliterature.about_app.ui.AboutAppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val aboutAppUiModule = module {
    viewModelOf(::AboutAppViewModel)
}
