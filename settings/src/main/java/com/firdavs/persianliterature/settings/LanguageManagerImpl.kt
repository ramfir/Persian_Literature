package com.firdavs.persianliterature.settings

import android.content.Context
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import androidx.core.content.edit

class LanguageManagerImpl : LanguageManager {

    override fun getSavedLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, Language.ENGLISH.code) ?: Language.ENGLISH.code
        val language = Language.entries.find { it.code == code } ?: Language.ENGLISH
        return language
    }

    override fun setLanguage(context: Context, language: Language) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_LANGUAGE, language.code) }
    }

    companion object {
        private const val PREF_NAME = "language_preferences"
        private const val KEY_LANGUAGE = "selected_language"
    }
}
