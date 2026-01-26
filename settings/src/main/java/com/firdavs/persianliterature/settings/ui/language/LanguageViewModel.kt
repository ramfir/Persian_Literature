package com.firdavs.persianliterature.settings.ui.language

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val application: Application,
    private val languageManager: LanguageManager,
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val poemRepository: PoemRepository
) : BaseViewModel<LanguageUiState>(LanguageUiState()) {

    init {
        val savedLanguage = languageManager.getSavedLanguage(application)
        post {
            it.copy(selectedLanguage = savedLanguage)
        }
    }

    fun onLanguageSelected(language: Language) {
        post {
            it.copy(selectedLanguage = language)
        }
    }

    fun onApplyClick() {
        val language = state.value.selectedLanguage
        languageManager.setLanguage(application, language)
        post { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            runCatching {
                // Refresh all data with the new language
                authorRepository.fetchAuthors()
                worksRepository.fetchWorks()
                quizRepository.fetchQuizzes()
                questionRepository.fetchQuestions()
                poemRepository.fetchPoems()
            }.onFailure { error ->
                Log.e(TAG, "onApplyClick error", error)
                post { it.copy(isRefreshing = false, showErrorToast = true) }
            }.onSuccess {
                post { it.copy(isRefreshing = false, showSuccessToast = true) }
            }
        }
    }

    fun resetShowErrorToastFlag() {
        post { it.copy(showErrorToast = false) }
    }

    fun resetShowSuccessToastFlag() {
        post { it.copy(showSuccessToast = false) }
    }

    companion object {
        private const val TAG = "LanguageViewModel"
    }
}
