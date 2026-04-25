package com.firdavs.persianliterature.app.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.manager.NewWorksNotificationManager
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.settings.api.NotificationManager
import com.firdavs.persianliterature.settings.api.UpdateInfo
import com.firdavs.persianliterature.settings.api.UpdateManager
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
    private val notificationManager: NotificationManager,
    private val updateManager: UpdateManager,
    private val newWorksNotificationManager: NewWorksNotificationManager
) : BaseViewModel<MainActivityUiState>(MainActivityUiState()) {

    init {
        checkFirstLaunch()
        observeUpdateState()
        checkForUpdates()
    }

    private fun checkFirstLaunch() {
        val prefs = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val isFirstLaunch = prefs.getBoolean(KEY_FIRST_LAUNCH, true)

        if (isFirstLaunch) {
            post { it.copy(showLanguageSelectionDialog = true) }
        } else {
            fetchAllData()
            checkForNewWorks()
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

    private fun checkForNewWorks() {
        viewModelScope.launch {
            try {
                val newWorks = newWorksNotificationManager.checkForNewWorks()
                if (newWorks.isNotEmpty()) {
                    post { it.copy(newWorks = newWorks) }
                }
            } catch (e: Exception) {
                // Log error, don't block app launch
                Log.e(TAG, "Failed to check new works", e)
            }
        }
    }

    fun onNewWorksDialogShown() {
        viewModelScope.launch {
            post { it.copy(newWorks = emptyList()) }
        }
    }

    fun onNewWorkClicked(workId: String) {
        post { it.copy(navigationWorkId = workId, newWorks = emptyList()) }
    }

    fun clearNavigationWorkId() {
        post { it.copy(navigationWorkId = null) }
    }

    fun setNotificationPoemId(poemId: String?) {
        post { it.copy(notificationPoemId = poemId) }
    }

    fun clearNotificationPoemId() {
        post { it.copy(notificationPoemId = null) }
    }

    private fun fetchAllData() {
        fetchAuthors()
        fetchWorks()
        fetchQuizzes()
        fetchQuestions()
        fetchPoems()
    }

    fun onLanguageSelected(language: Language) {
        // Set the selected language
        languageManager.setLanguage(application, language)
        fetchAllData()

        // Mark first launch as completed
        val prefs = application.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_FIRST_LAUNCH, false) }

        // Only show notification permission dialog on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Dismiss language selection dialog and show notification permission explanation dialog
            post { it.copy(showLanguageSelectionDialog = false, showNotificationPermissionDialog = true) }
        } else {
            // On Android 12 and below, permission is granted at install time
            // Just enable notifications automatically
            post { it.copy(showLanguageSelectionDialog = false) }
            onNotificationPermissionResult(true)
        }
    }

    fun onNotificationPermissionAllowClicked() {
        // Dismiss the explanation dialog and request the actual system permission
        post { it.copy(showNotificationPermissionDialog = false, requestNotificationPermission = true) }
    }

    fun onNotificationPermissionSkipClicked() {
        // Just dismiss the explanation dialog without requesting permission
        post { it.copy(showNotificationPermissionDialog = false) }
    }

    fun checkForUpdates() {
        viewModelScope.launch {
            val updateInfo = updateManager.checkForUpdate()
            when (updateInfo) {
                is UpdateInfo.UpdateAvailable -> {
                    post { it.copy(showUpdateDialog = true) }
                }
                is UpdateInfo.UpdateDownloaded -> {
                    post { it.copy(showInstallPrompt = true) }
                }
                else -> { /* No update available */ }
            }
        }
    }

    private fun observeUpdateState() {
        viewModelScope.launch {
            updateManager.observeUpdateState().collect { state ->
                if (state.isUpdateDownloaded) {
                    post { it.copy(showInstallPrompt = true) }
                }
            }
        }
    }

    fun onUpdateDialogDismissed() {
        post { it.copy(showUpdateDialog = false) }
    }

    fun onUpdateClicked(activity: Activity) {
        post { it.copy(showUpdateDialog = false) }
        viewModelScope.launch {
            updateManager.startFlexibleUpdate(activity)
        }
    }

    fun onInstallUpdateClicked() {
        updateManager.completeUpdate()
        post { it.copy(showInstallPrompt = false) }
    }

    fun onInstallPromptDismissed() {
        post { it.copy(showInstallPrompt = false) }
    }

    fun checkIfUpdateDownloaded() {
        viewModelScope.launch {
            val updateInfo = updateManager.checkForUpdate()
            if (updateInfo is UpdateInfo.UpdateDownloaded) {
                post { it.copy(showInstallPrompt = true) }
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val PREF_NAME = "app_preferences"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"
    }
}
