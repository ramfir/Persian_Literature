package com.firdavs.persianliterature.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.Locale

class LanguageManagerImpl(private val context: Context) : LanguageManager {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override fun getSavedLanguage(context: Context): Language {
        val code = prefs.getString(KEY_LANGUAGE, Language.ENGLISH.code) ?: Language.ENGLISH.code
        return Language.entries.find { it.code == code } ?: Language.ENGLISH
    }

    override fun setLanguage(context: Context, language: Language) {
        prefs.edit { putString(KEY_LANGUAGE, language.code) }
    }

    override val currentLanguage: Flow<Language> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == KEY_LANGUAGE) {
                trySend(getSavedLanguage(context))
            }
        }

        prefs.registerOnSharedPreferenceChangeListener(listener)
        // Emit initial value
        trySend(getSavedLanguage(context))

        awaitClose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }.distinctUntilChanged()

    override val currentLocale: Flow<Locale> = currentLanguage.map { language ->
        Locale(language.code)
    }

    override val systemLocale: Locale
        get() = Locale.getDefault()

    companion object {
        private const val PREF_NAME = "language_preferences"
        private const val KEY_LANGUAGE = "selected_language"
    }
}
