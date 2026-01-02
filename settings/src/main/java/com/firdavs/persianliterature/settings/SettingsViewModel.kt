package com.firdavs.persianliterature.settings

import android.app.Application
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager

class SettingsViewModel(
    private val application: Application,
    private val languageManager: LanguageManager
) : BaseViewModel<SettingsUiState>(SettingsUiState()) {

    init {
        val savedLanguage = languageManager.getSavedLanguage(application)
        post {
            it.copy(selectedLanguage = savedLanguage)
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
}
