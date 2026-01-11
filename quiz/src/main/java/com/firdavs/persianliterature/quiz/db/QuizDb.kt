package com.firdavs.persianliterature.quiz.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.firdavs.persianliterature.quiz.db.converter.StringListConverter
import com.firdavs.persianliterature.quiz.db.dao.QuizDao
import com.firdavs.persianliterature.quiz.db.dao.QuestionDao
import com.firdavs.persianliterature.quiz.db.dao.QuizProgressDao
import com.firdavs.persianliterature.quiz.db.model.QuizEntity
import com.firdavs.persianliterature.quiz.db.model.QuestionEntity
import com.firdavs.persianliterature.quiz.db.model.QuizProgressEntity

@Database(
    entities = [
        QuizEntity::class,
        QuestionEntity::class,
        QuizProgressEntity::class
    ],
    version = QuizDb.VERSION
)
@TypeConverters(StringListConverter::class)
abstract class QuizDb : RoomDatabase() {

    abstract fun getQuizDao(): QuizDao
    abstract fun getQuestionDao(): QuestionDao
    abstract fun getQuizProgressDao(): QuizProgressDao

    companion object {
        const val VERSION = 1
        const val QUIZZES = "quizzes"
        const val QUESTIONS = "questions"
        const val QUIZ_PROGRESS = "quiz_progress"
        const val DATABASE_NAME = "quizDb.db"
    }
}
