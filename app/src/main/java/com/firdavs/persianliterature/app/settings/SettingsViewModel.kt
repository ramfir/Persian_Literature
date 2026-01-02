package com.firdavs.persianliterature.app.settings

import android.app.Application
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.core.presentation.UiState

class SettingsViewModel(
    private val application: Application
) : BaseViewModel<SettingsUiState>(UiState.Success(SettingsUiState())) {

    init {
        val savedLanguage = LanguageManager.getSavedLanguage(application)
        reduce {
            copy(selectedLanguage = savedLanguage)
        }
    }

    fun onLanguageSelected(language: LanguageManager.Language) {
        reduce {
            copy(selectedLanguage = language)
        }
    }

    fun onApplyClick() {
        // The actual language change and activity restart is handled in the composable
        // This is just a placeholder for any additional logic if needed
    }
}
