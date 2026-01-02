package com.firdavs.persianliterature.settings

import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState
import com.firdavs.persianliterature.settings.api.Language

data class SettingsUiState(
    val chapters: List<Chapter> = Chapter.all,
    val selectedLanguage: Language = Language.ENGLISH
) : UiState()
