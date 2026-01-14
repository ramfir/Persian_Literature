package com.firdavs.persianliterature.settings

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import kotlinx.coroutines.launch

class LanguageViewModel(
    private val application: Application,
    private val languageManager: LanguageManager,
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository
) : BaseViewModel<LanguageUiState>(LanguageUiState()) {

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
        viewModelScope.launch {
            authorRepository.fetchAuthors()
            worksRepository.fetchWorks()
        }
    }
}
