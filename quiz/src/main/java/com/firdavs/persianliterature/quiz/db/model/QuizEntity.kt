package com.firdavs.persianliterature.quiz.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz_api.model.Quiz
import com.firdavs.persianliterature.quiz_api.model.QuizCategory
import com.firdavs.persianliterature.quiz_api.model.QuizDifficulty

@Entity(tableName = QuizDb.QUIZZES)
data class QuizEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val category: String,
    val totalQuestions: Int,
    val passingScore: Int,
    val order: Int
)

fun QuizEntity.toDomain() = Quiz(
    id = id,
    title = title,
    description = description,
    difficulty = QuizDifficulty.valueOf(difficulty),
    category = QuizCategory.valueOf(category),
    totalQuestions = totalQuestions,
    passingScore = passingScore,
    order = order
)

fun List<QuizEntity>.toDomain() = map { it.toDomain() }
