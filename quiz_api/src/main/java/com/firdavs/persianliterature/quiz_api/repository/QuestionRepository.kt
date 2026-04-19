package com.firdavs.persianliterature.quiz_api.repository

import com.firdavs.persianliterature.quiz_api.model.Question
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {
    suspend fun fetchQuestions()
    fun getQuestionsByQuizId(quizId: String): Flow<List<Question>>
}
