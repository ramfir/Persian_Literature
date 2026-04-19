package com.firdavs.persianliterature.settings.ui.main

import android.app.Application
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.settings.api.NotificationManager

class SettingsViewModel(
    private val application: Application,
    private val notificationManager: NotificationManager
) : BaseViewModel<SettingsUiState>(SettingsUiState()) {

    init {
        val notificationsEnabled = notificationManager.isNotificationEnabled(application)
        post {
            it.copy(notificationsEnabled = notificationsEnabled)
        }
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
