package com.firdavs.persianliterature.app.settings

import com.firdavs.persianliterature.core.model.Chapter

data class SettingsUiState(
    val chapters: List<Chapter> = Chapter.all,
    val selectedLanguage: LanguageManager.Language = LanguageManager.Language.ENGLISH
)
