package com.firdavs.persianliterature.app.ui

import com.firdavs.persianliterature.core.presentation.UiState
import com.firdavs.persianliterature.settings.api.UpdateInfo

data class MainActivityUiState(
    val notificationPoemId: String? = null,
    val showWelcomeDialog: Boolean = false,
    val showLanguageSelectionDialog: Boolean = false,
    val showNotificationPermissionDialog: Boolean = false,
    val requestNotificationPermission: Boolean = false,
    val showUpdateDialog: Boolean = false,
    val updateInfo: UpdateInfo = UpdateInfo.NoUpdateAvailable,
    val showInstallPrompt: Boolean = false
) : UiState()
