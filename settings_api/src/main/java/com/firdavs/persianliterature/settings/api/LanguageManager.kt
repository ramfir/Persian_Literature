package com.firdavs.persianliterature.settings.api

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface LanguageManager : LocaleHolder {
    fun getSavedLanguage(context: Context): Language
    fun setLanguage(context: Context, language: Language)

    /**
     * Flow that emits the current selected language.
     * Emits whenever the user changes the language.
     */
    val currentLanguage: Flow<Language>
}
