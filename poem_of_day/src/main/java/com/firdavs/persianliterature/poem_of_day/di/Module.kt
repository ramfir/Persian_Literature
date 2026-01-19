package com.firdavs.persianliterature.poem_of_day.di

import com.firdavs.persianliterature.poem_of_day.PoemOfDayViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val poemOfDayModule = module {
    viewModelOf(::PoemOfDayViewModel)
}
