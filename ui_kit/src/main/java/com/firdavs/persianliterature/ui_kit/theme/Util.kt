package com.firdavs.persianliterature.ui_kit.theme

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.staticCompositionLocalOf

fun calculateTypography(windowWidthSizeClass: WindowWidthSizeClass) = when (windowWidthSizeClass) {
    WindowWidthSizeClass.Expanded -> ExpandedTypography
    else -> CompactTypography
}

inline fun <reified T : Any> requiredStaticCompositionLocalOf() = staticCompositionLocalOf<T> {
    error("No ${T::class.simpleName} instances provided")
}
