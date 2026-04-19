package com.firdavs.persianliterature.quiz_api.repository

import com.firdavs.persianliterature.quiz_api.model.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun fetchQuizzes()
    fun getQuizzes(): Flow<List<Quiz>>
    fun getQuiz(id: String): Flow<Quiz>
}
