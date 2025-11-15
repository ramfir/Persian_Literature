package com.firdavs.persianliterature.ui.kit.theme

import androidx.compose.ui.text.TextStyle

interface AppTypography {
    val h2TextStyle: TextStyle
    val h3TextStyle: TextStyle
    val h4TextStyle: TextStyle
    val h5TextStyle: TextStyle
    val h5BoldTextStyle: TextStyle
    val h6TextStyle: TextStyle
    val t1TextStyle: TextStyle
    val t2TextStyle: TextStyle
}

object CompactTypography : AppTypography {
    override val h2TextStyle = h2compact
    override val h3TextStyle = h3compact
    override val h4TextStyle = h4compact
    override val h5TextStyle = h5compact
    override val h5BoldTextStyle = h5BoldCompact
    override val h6TextStyle = h6compact
    override val t1TextStyle = t1compact
    override val t2TextStyle = t2compact
}

object ExpandedTypography : AppTypography {
    override val h2TextStyle = h2compact
    override val h3TextStyle = h3compact
    override val h4TextStyle = h4compact
    override val h5TextStyle = h5compact
    override val h5BoldTextStyle = h5BoldCompact
    override val h6TextStyle = h6compact
    override val t1TextStyle = t1compact
    override val t2TextStyle = t2compact
}
