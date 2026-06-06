package com.firdavs.persianliterature.ui.kit.theme

import androidx.compose.material3.lightColorScheme

val LightColorScheme = lightColorScheme(
    primary = Gold,
    onPrimary = DarkOnPrimary,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldLight,

    error = RedError,
    onError = RedDark,
    errorContainer = Red,
    onErrorContainer = MelonLight,

    tertiary = Green,

    background = GreyLight,
    onBackground = GoldLight,

    outline = Grey
)

val DarkColorScheme = lightColorScheme(
    primary = Gold,
    onPrimary = GoldOnPrimary,
    primaryContainer = GoldContainer,
    onPrimaryContainer = GoldLight,

    error = RedError,
    onError = RedDark,
    errorContainer = Red,
    onErrorContainer = MelonLight,

    tertiary = Green,

    background = GreyLight,
    onBackground = GoldLight,

    outline = Grey
)
