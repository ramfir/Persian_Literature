package com.firdavs.persianliterature.app.ui

import com.firdavs.persianliterature.author_api.model.NewWorkItem
import com.firdavs.persianliterature.core.presentation.UiState

data class MainActivityUiState(
    val notificationPoemId: String? = null,
    val showLanguageSelectionDialog: Boolean = false,
    val showNotificationPermissionDialog: Boolean = false,
    val requestNotificationPermission: Boolean = false,
    val showUpdateDialog: Boolean = false,
    val showInstallPrompt: Boolean = false,
    val newWorks: List<NewWorkItem> = emptyList(),
    val navigationWorkId: String? = null
) : UiState()
