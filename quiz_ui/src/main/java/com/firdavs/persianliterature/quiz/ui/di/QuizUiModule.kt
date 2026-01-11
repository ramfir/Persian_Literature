package com.firdavs.persianliterature.quiz.ui.di

import com.firdavs.persianliterature.quiz.ui.list.QuizListViewModel
import com.firdavs.persianliterature.quiz.ui.play.QuizPlayViewModel
import com.firdavs.persianliterature.quiz.ui.result.QuizResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val quizUiModule = module {
    viewModelOf(::QuizListViewModel)

    viewModel { (args: Array<Any?>) ->
        QuizPlayViewModel(
            args.first() as String,
            get(),
            get(),
            get()
        )
    }

    viewModel { (args: Array<Any?>) ->
        QuizResultViewModel(
            args.first() as String,
            get(),
            get()
        )
    }
}
