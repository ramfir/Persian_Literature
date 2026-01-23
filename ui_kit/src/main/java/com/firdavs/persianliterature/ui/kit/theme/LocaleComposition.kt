package com.firdavs.persianliterature.ui.kit.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

/**
 * CompositionLocal for providing the current app locale to the entire Compose tree.
 *
 * This allows all composables to access the current locale without prop drilling,
 * and automatically recompose when the locale changes.
 */
val LocalAppLocale = compositionLocalOf<Locale> {
    error("No Locale provided")
}

/**
 * Returns a localized context with the current app locale applied.
 * Use this in composables that need a Context with the correct locale.
 */
@Composable
@ReadOnlyComposable
fun localizedContext(): Context {
    val context = LocalContext.current
    val locale = LocalAppLocale.current
    return createLocalizedContext(context, locale)
}

/**
 * Creates a new context with the specified locale applied to its configuration.
 */
@Suppress("DEPRECATION")
private fun createLocalizedContext(baseContext: Context, locale: Locale): Context {
    val configuration = baseContext.resources.configuration
    configuration.setLocale(locale)
    return baseContext.createConfigurationContext(configuration)
}
