package com.firdavs.persianliterature.settings

import android.app.Application
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.settings.api.NotificationManager

class SettingsViewModel(
    private val application: Application,
    private val languageManager: LanguageManager,
    private val notificationManager: NotificationManager
) : BaseViewModel<SettingsUiState>(SettingsUiState()) {

    init {
        val savedLanguage = languageManager.getSavedLanguage(application)
        val notificationsEnabled = notificationManager.isNotificationEnabled(application)
        post {
            it.copy(
                selectedLanguage = savedLanguage,
                notificationsEnabled = notificationsEnabled
            )
        }
    }

    fun onLanguageSelected(language: Language) {
        post {
            it.copy(selectedLanguage = language)
        }
    }

    fun onApplyClick(language: Language) {
        languageManager.setLanguage(application, language)
    }

    fun onNotificationToggle(enabled: Boolean) {
        notificationManager.setNotificationEnabled(application, enabled)

        if (enabled) {
            notificationManager.scheduleNotification(application)
        } else {
            notificationManager.cancelNotification(application)
        }

        post {
            it.copy(notificationsEnabled = enabled)
        }
    }
}
