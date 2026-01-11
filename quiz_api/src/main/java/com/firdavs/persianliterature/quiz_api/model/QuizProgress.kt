package com.firdavs.persianliterature.quiz_api.model

data class QuizProgress(
    val id: String,
    val quizId: String,
    val score: Int,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val completedAt: Long,
    val timeSpentSeconds: Int,
    val answers: List<UserAnswer>,
    val titleEarned: String
)

data class UserAnswer(
    val questionId: String,
    val selectedAnswer: String,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)
