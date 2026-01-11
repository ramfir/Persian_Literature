package com.firdavs.persianliterature.quiz_api.repository

import com.firdavs.persianliterature.quiz_api.model.QuizAttemptSummary
import com.firdavs.persianliterature.quiz_api.model.QuizProgress
import kotlinx.coroutines.flow.Flow

interface QuizProgressRepository {
    suspend fun saveProgress(progress: QuizProgress)
    fun getProgressByQuizId(quizId: String): Flow<List<QuizProgress>>
    fun getBestAttempt(quizId: String): Flow<QuizProgress?>
    fun getAllAttemptSummaries(): Flow<List<QuizAttemptSummary>>
    suspend fun calculateScore(correctAnswers: Int, totalQuestions: Int): Int
    fun determineTitleEarned(score: Int, difficulty: String): String
}
