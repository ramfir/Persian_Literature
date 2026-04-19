package com.firdavs.persianliterature.settings.api

import kotlinx.coroutines.flow.Flow
import java.util.Locale

/**
 * Reactive holder for application locale.
 * Provides Flow-based observation of locale changes.
 */
interface LocaleHolder {
    /**
     * Flow that emits the current application locale.
     * Emits whenever the user changes the language.
     */
    val currentLocale: Flow<Locale>

    /**
     * Returns the system default locale.
     */
    val systemLocale: Locale
}
