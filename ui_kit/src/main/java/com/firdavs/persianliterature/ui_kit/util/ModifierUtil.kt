package com.firdavs.persianliterature.ui_kit.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Modifier.thenIf(condition: Boolean, block: @Composable Modifier.() -> Modifier) =
    if (condition) then(Modifier.block()) else this

@Composable
fun <T : Any> Modifier.thenIfNotNull(obj: T?, block: @Composable Modifier.(T) -> Modifier) =
    if (obj != null) then(Modifier.block(obj)) else this
