package com.firdavs.persianliterature.author.ui.di

import com.firdavs.persianliterature.author.ui.list.AuthorsListViewModel
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapperImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authorUiModule = module {
    viewModelOf(::AuthorsListViewModel)
    factoryOf(::AuthorUiMapperImpl) bind AuthorUiMapper::class
}
