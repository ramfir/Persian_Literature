package com.firdavs.persianliterature.app.ui

import android.app.Application
import android.content.Context
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
import com.firdavs.persianliterature.settings.api.NotificationManager
import kotlinx.coroutines.launch
import androidx.core.content.edit

class MainViewModel(
    private val application: Application,
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val poemRepository: PoemRepository,
    private val languageManager: LanguageManager,
    private val notificationManager: NotificationManager
) : BaseViewModel<MainActivityUiState>(MainActivityUiState()) {

    init {
        checkFirstLaunch()
        fetchAuthors()
        fetchWorks()
        fetchQuizzes()
        fetchQuestions()
        fetchPoems()
    }

    private fun checkFirstLaunch() {
        val prefs = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

        if (isFirstLaunch) {
            post { it.copy(showLanguageSelectionDialog = true) }
        }
    }

    fun onNotificationPermissionResult(granted: Boolean) {
        if (granted) {
            notificationManager.setNotificationEnabled(application, true)
            notificationManager.scheduleNotification(application)
        }
        post { it.copy(requestNotificationPermission = false) }
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

    fun dismissWelcomeDialog() {
        post { it.copy(showWelcomeDialog = false) }
    }

    fun onLanguageSelected(language: Language) {
        // Set the selected language
        languageManager.setLanguage(application, language)

        // Dismiss the language selection dialog
        post { it.copy(showLanguageSelectionDialog = false, requestNotificationPermission = true) }

        // Mark first launch as completed
        val prefs = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_FIRST_LAUNCH, false) }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val PREF_NAME = "app_preferences"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"
    }
}
