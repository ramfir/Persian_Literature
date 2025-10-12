package com.firdavs.persianliterature.ui_kit.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

val LocalColors = requiredStaticCompositionLocalOf<ColorScheme>()
val LocalTypography = requiredStaticCompositionLocalOf<AppTypography>()
val LocalShapes = requiredStaticCompositionLocalOf<AppShapes>()

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)
    BaseTheme(
        windowSizeClass = windowSizeClass,
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppPreviewTheme(content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    val mockWindowSizeClass = WindowSizeClass.calculateFromSize(size)
    BaseTheme(
        windowSizeClass = mockWindowSizeClass,
        darkTheme = false,
        dynamicColor = false,
        content = content
    )
}

@Composable
private fun BaseTheme(
    windowSizeClass: WindowSizeClass,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val typography = calculateTypography(windowSizeClass.widthSizeClass)
    val shapes = AppShapes.Default

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    CompositionLocalProvider(
        LocalColors provides colorScheme,
        LocalTypography provides typography,
        LocalShapes provides shapes,
        content = content
    )
}

object AppTheme {
    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColors.current
    val typography: AppTypography
        @Composable @ReadOnlyComposable get() = LocalTypography.current
    val shapes: AppShapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current
}
