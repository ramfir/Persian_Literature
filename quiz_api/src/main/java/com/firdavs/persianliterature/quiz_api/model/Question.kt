package com.firdavs.persianliterature.quiz_api.model

data class Question(
    val id: String,
    val quizId: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String,
    val order: Int,
    val relatedAuthorId: String?
)
