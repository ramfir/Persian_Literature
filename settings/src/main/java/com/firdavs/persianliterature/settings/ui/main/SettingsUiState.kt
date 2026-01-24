package com.firdavs.persianliterature.settings.ui.main

import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class SettingsUiState(
    val chapters: List<Chapter> = Chapter.all,
    val notificationsEnabled: Boolean = false
) : UiState()
