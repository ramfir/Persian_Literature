package com.firdavs.persianliterature.quiz_api.model

data class Quiz(
    val id: String,
    val title: String,
    val description: String,
    val difficulty: QuizDifficulty,
    val category: QuizCategory,
    val totalQuestions: Int,
    val passingScore: Int,
    val order: Int
)

enum class QuizDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

enum class QuizCategory {
    MATCH_POET_TO_POEM,
    LITERARY_PERIODS,
    COMPLETE_THE_VERSE,
    AUTHOR_BIOGRAPHY,
    WORK_IDENTIFICATION
}
