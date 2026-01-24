package com.firdavs.persianliterature.app.ui

import com.firdavs.persianliterature.core.presentation.UiState

data class MainActivityUiState(
    val notificationPoemId: String? = null,
    val showWelcomeDialog: Boolean = false
) : UiState()
