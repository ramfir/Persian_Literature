package com.firdavs.persianliterature.ui.kit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

interface AppShapes {
    val small: RoundedCornerShape
    val medium: RoundedCornerShape
    val large: RoundedCornerShape
    val extraLarge: RoundedCornerShape

    object Default : AppShapes {
        override val small = RoundedCornerShape(8.dp)
        override val medium = RoundedCornerShape(12.dp)
        override val large = RoundedCornerShape(16.dp)
        override val extraLarge = RoundedCornerShape(24.dp)
    }
}
