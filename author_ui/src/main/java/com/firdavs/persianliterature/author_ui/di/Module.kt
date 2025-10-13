package com.firdavs.persianliterature.author_ui.di

import com.firdavs.persianliterature.author_ui.list.AuthorsListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authorUiModule = module {
    viewModelOf(::AuthorsListViewModel)
}
