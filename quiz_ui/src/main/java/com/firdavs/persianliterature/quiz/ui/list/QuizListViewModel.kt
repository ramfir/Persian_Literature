package com.firdavs.persianliterature.quiz.ui.list

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizListViewModel(
    private val quizRepository: QuizRepository,
    private val quizProgressRepository: QuizProgressRepository
) : BaseViewModel<QuizListUiState>(QuizListUiState()) {

    init {
        observeQuizzes()
        observeAttemptSummaries()
    }

    private fun observeQuizzes() {
        viewModelScope.launch {
            quizRepository.getQuizzes().collect { quizzes ->
                post { it.copy(quizzes = quizzes, isLoading = false) }
            }
        }
    }

    private fun observeAttemptSummaries() {
        viewModelScope.launch {
            quizProgressRepository.getAllAttemptSummaries().collect { summaries ->
                post { it.copy(attemptSummaries = summaries) }
            }
        }
    }
}
