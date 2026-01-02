package com.firdavs.persianliterature.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager

class LanguageManagerImpl : LanguageManager {
    private val PREF_NAME = "language_preferences"
    private val KEY_LANGUAGE = "selected_language"

    override fun getSavedLanguage(context: Context): Language {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, Language.ENGLISH.code) ?: Language.ENGLISH.code
        return Language.values().find { it.code == code } ?: Language.ENGLISH
    }

    override fun setLanguage(context: Context, language: Language) {
        // Save preference
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()

        // Apply locale - AppCompatDelegate handles activity recreation automatically
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
