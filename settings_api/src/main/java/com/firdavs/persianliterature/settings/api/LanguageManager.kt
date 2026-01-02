package com.firdavs.persianliterature.settings.api

import android.content.Context

interface LanguageManager {
    fun getSavedLanguage(context: Context): Language
    fun setLanguage(context: Context, language: Language)
}
