package com.firdavs.persianliterature.quiz.di

import androidx.room.Room
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz.db.dao.QuizDao
import com.firdavs.persianliterature.quiz.db.dao.QuestionDao
import com.firdavs.persianliterature.quiz.db.dao.QuizProgressDao
import com.firdavs.persianliterature.quiz.repository.QuizRepositoryImpl
import com.firdavs.persianliterature.quiz.repository.QuestionRepositoryImpl
import com.firdavs.persianliterature.quiz.repository.QuizProgressRepositoryImpl
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val quizModule = module {
    singleOf(::QuizRepositoryImpl) bind QuizRepository::class
    singleOf(::QuestionRepositoryImpl) bind QuestionRepository::class
    singleOf(::QuizProgressRepositoryImpl) bind QuizProgressRepository::class

    single<QuizDb> {
        Room.databaseBuilder(
            androidContext(),
            QuizDb::class.java,
            QuizDb.DATABASE_NAME
        ).build()
    }

    single<QuizDao> { get<QuizDb>().getQuizDao() }
    single<QuestionDao> { get<QuizDb>().getQuestionDao() }
    single<QuizProgressDao> { get<QuizDb>().getQuizProgressDao() }
}
