package com.firdavs.persianliterature.quiz_api.model

data class QuizAttemptSummary(
    val quizId: String,
    val quizTitle: String,
    val bestScore: Int,
    val attemptCount: Int,
    val lastAttemptDate: Long?,
    val highestTitleEarned: String?
)
