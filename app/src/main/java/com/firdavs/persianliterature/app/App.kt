package com.firdavs.persianliterature.app

import android.app.Application
import com.firdavs.persianliterature.app.di.appModule
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
    }
}
