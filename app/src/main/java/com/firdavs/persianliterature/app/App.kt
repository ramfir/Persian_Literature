package com.firdavs.persianliterature.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.firdavs.persianliterature.app.di.appModule
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseFirestore.setLoggingEnabled(true)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }

        // Initialize app locale from saved preference
        val languageManager = org.koin.java.KoinJavaComponent.get<LanguageManager>(LanguageManager::class.java)
        val savedLanguage = languageManager.getSavedLanguage(this)
        val localeList = LocaleListCompat.forLanguageTags(savedLanguage.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
