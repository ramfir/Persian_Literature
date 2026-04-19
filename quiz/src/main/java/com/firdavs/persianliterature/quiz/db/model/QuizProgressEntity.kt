package com.firdavs.persianliterature.quiz.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz_api.model.QuizProgress
import com.firdavs.persianliterature.quiz_api.model.UserAnswer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = QuizDb.QUIZ_PROGRESS)
data class QuizProgressEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val completedAt: Long,
    val timeSpentSeconds: Int,
    val answers: String,
    val titleEarned: String
)

fun QuizProgressEntity.toDomain() = QuizProgress(
    id = id,
    quizId = quizId,
    score = score,
    correctAnswers = correctAnswers,
    totalQuestions = totalQuestions,
    completedAt = completedAt,
    timeSpentSeconds = timeSpentSeconds,
    answers = parseAnswers(answers),
    titleEarned = titleEarned
)

fun List<QuizProgressEntity>.toDomain() = map { it.toDomain() }

private fun parseAnswers(json: String): List<UserAnswer> {
    return try {
        val listType = object : TypeToken<List<UserAnswer>>() {}.type
        Gson().fromJson(json, listType)
    } catch (e: Exception) {
        emptyList()
    }
}
