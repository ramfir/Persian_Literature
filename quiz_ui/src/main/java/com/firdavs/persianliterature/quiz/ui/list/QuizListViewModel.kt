package com.firdavs.persianliterature.quiz.ui.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import kotlinx.coroutines.launch

class QuizListViewModel(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
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

    fun onRefreshClick() {
        post { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            runCatching {
                quizRepository.fetchQuizzes()
                questionRepository.fetchQuestions()
            }.onFailure { error ->
                Log.e(TAG, "onRefreshClick error", error)
            }.onSuccess {
                post { it.copy(isRefreshing = false) }
            }
        }
    }

    companion object {
        private const val TAG = "QuizListViewModel"
    }
}
