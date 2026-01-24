package com.firdavs.persianliterature.quiz.ui.list

import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState
import com.firdavs.persianliterature.quiz_api.model.Quiz
import com.firdavs.persianliterature.quiz_api.model.QuizAttemptSummary

data class QuizListUiState(
    val quizzes: List<Quiz> = emptyList(),
    val attemptSummaries: List<QuizAttemptSummary> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val showToast: Boolean = false,
    val showErrorToast: Boolean = false,
    val chapters: List<Chapter> = Chapter.all
) : UiState()
