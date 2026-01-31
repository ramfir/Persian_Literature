package com.firdavs.persianliterature.quiz.ui.play

import com.firdavs.persianliterature.core.presentation.UiState
import com.firdavs.persianliterature.quiz_api.model.Question
import com.firdavs.persianliterature.quiz_api.model.Quiz

data class QuizPlayUiState(
    val quiz: Quiz? = null,
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userAnswers: Map<String, String> = emptyMap(),
    val selectedButUnconfirmedAnswer: String? = null,
    val showExplanation: Boolean = false,
    val isSubmitted: Boolean = false,
    val progressId: String? = null,
    val startTime: Long = System.currentTimeMillis(),
    val questionStartTime: Long = System.currentTimeMillis(),
    val showQuitDialog: Boolean = false
) : UiState() {
    val currentQuestion: Question?
        get() = questions.getOrNull(currentQuestionIndex)

    val progress: Float
        get() = if (questions.isEmpty()) 0f else (currentQuestionIndex + 1).toFloat() / questions.size

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.size - 1
}
