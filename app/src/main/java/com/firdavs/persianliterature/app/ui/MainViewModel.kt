package com.firdavs.persianliterature.app.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val poemRepository: PoemRepository
) : BaseViewModel<MainActivityUiState>(MainActivityUiState()) {

    init {
        fetchAuthors()
        fetchWorks()
        fetchQuizzes()
        fetchQuestions()
        fetchPoems()
    }

    private fun fetchAuthors() {
        viewModelScope.launch {
            runCatching {
                authorRepository.fetchAuthors()
            }.onFailure {
                Log.e(TAG, "fetchAuthors error ", it)
            }
        }
    }

    private fun fetchWorks() {
        viewModelScope.launch {
            runCatching {
                worksRepository.fetchWorks()
            }.onFailure {
                Log.e(TAG, "fetchWorks error ", it)
            }
        }
    }

    private fun fetchQuizzes() {
        viewModelScope.launch {
            runCatching {
                quizRepository.fetchQuizzes()
            }.onFailure {
                Log.e(TAG, "fetchQuizzes error ", it)
            }
        }
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            runCatching {
                questionRepository.fetchQuestions()
            }.onFailure {
                Log.e(TAG, "fetchQuestions error ", it)
            }
        }
    }

    private fun fetchPoems() {
        viewModelScope.launch {
            runCatching {
                poemRepository.fetchPoems()
            }.onFailure {
                Log.e(TAG, "fetchPoems error ", it)
            }
        }
    }

    fun setNotificationPoemId(poemId: String?) {
        post { it.copy(notificationPoemId = poemId) }
    }

    fun clearNotificationPoemId() {
        post { it.copy(notificationPoemId = null) }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
