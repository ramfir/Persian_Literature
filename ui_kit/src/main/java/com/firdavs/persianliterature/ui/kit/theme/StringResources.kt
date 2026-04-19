package com.firdavs.persianliterature.ui.kit.theme

import android.content.res.Resources
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * Custom stringResource that uses LocalAppLocale instead of system locale.
 * This allows runtime language changes without app restart.
 */
@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int): String {
    val resources = localizedResources()
    return resources.getString(id)
}

/**
 * Custom stringResource with format arguments that uses LocalAppLocale.
 */
@Composable
@ReadOnlyComposable
fun stringResource(@StringRes id: Int, vararg formatArgs: Any): String {
    val resources = localizedResources()
    return resources.getString(id, *formatArgs)
}

/**
 * Custom pluralStringResource that uses LocalAppLocale.
 */
@Composable
@ReadOnlyComposable
fun pluralStringResource(
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any = emptyArray()
): String {
    val resources = localizedResources()
    return resources.getQuantityString(id, quantity, *formatArgs)
}

/**
 * Custom pluralStringResource without format arguments that uses LocalAppLocale.
 */
@Composable
@ReadOnlyComposable
fun pluralStringResource(
    @PluralsRes id: Int,
    quantity: Int
): String {
    val resources = localizedResources()
    return resources.getQuantityString(id, quantity)
}

/**
 * Returns Resources configured with the current LocalAppLocale.
 * This ensures all string lookups use the app's selected language.
 */
@Composable
@ReadOnlyComposable
private fun localizedResources(): Resources {
    LocalConfiguration.current // Force recomposition on configuration changes
    return localizedContext().resources
}
