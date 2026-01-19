package com.firdavs.persianliterature.poem_of_day.di

import com.firdavs.persianliterature.poem_of_day.PoemOfDayViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val poemOfDayModule = module {
    viewModel { (args: Array<Any?>) ->
        PoemOfDayViewModel(
            poemId = args.firstOrNull() as? String,
            poemRepository = get()
        )
    }
}
