package com.firdavs.persianliterature.app.settings

import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class SettingsUiState(
    val chapters: List<Chapter> = Chapter.all,
    val selectedLanguage: LanguageManager.Language = LanguageManager.Language.ENGLISH
) : UiState()
