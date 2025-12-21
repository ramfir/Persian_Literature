package com.firdavs.persianliterature.ui.kit.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val default = TextStyle(fontFamily = FontFamily.Default, letterSpacing = 0.sp)
private val defaultW400 = default.copy(fontWeight = FontWeight.Normal)
private val defaultW500 = default.copy(fontWeight = FontWeight.Medium)

val h2compact = defaultW400.copy(fontSize = 32.sp, lineHeight = 40.sp)
val h3compact = defaultW400.copy(fontSize = 26.sp, lineHeight = 32.sp)
val h4compact = defaultW400.copy(fontSize = 22.sp, lineHeight = 26.sp)
val h5compact = defaultW400.copy(fontSize = 18.sp, lineHeight = 24.sp)
val h5BoldCompact = defaultW500.copy(fontSize = 18.sp, lineHeight = 24.sp)
val h6compact = defaultW500.copy(fontSize = 16.sp, lineHeight = 22.sp)
val t1compact = defaultW400.copy(fontSize = 16.sp, lineHeight = 22.sp)
val t2compact = defaultW400.copy(fontSize = 14.sp, lineHeight = 20.sp)
