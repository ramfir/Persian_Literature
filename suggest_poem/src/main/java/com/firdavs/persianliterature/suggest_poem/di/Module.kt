package com.firdavs.persianliterature.suggest_poem.di

import com.firdavs.persianliterature.suggest_poem.SuggestPoemViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val suggestPoemModule = module {
    viewModelOf(::SuggestPoemViewModel)
}
