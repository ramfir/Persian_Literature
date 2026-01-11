package com.firdavs.persianliterature.quiz.ui.result

import com.firdavs.persianliterature.core.presentation.UiState
import com.firdavs.persianliterature.quiz_api.model.QuizProgress
import com.firdavs.persianliterature.quiz_api.model.Quiz

data class QuizResultUiState(
    val quiz: Quiz? = null,
    val progress: QuizProgress? = null,
    val isPassed: Boolean = false
) : UiState()
