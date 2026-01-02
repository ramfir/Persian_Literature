package com.firdavs.persianliterature.app.settings

import android.app.Application
import com.firdavs.persianliterature.core.presentation.BaseViewModel

class SettingsViewModel(
    private val application: Application
) : BaseViewModel<SettingsUiState>(SettingsUiState()) {

    init {
        val savedLanguage = LanguageManager.getSavedLanguage(application)
        post {
            it.copy(selectedLanguage = savedLanguage)
        }
    }

    fun onLanguageSelected(language: LanguageManager.Language) {
        post {
            it.copy(selectedLanguage = language)
        }
    }

    fun onApplyClick() {
        // The actual language change and activity restart is handled in the composable
        // This is just a placeholder for any additional logic if needed
    }
}
