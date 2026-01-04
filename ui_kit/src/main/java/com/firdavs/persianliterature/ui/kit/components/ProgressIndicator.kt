package com.firdavs.persianliterature.ui.kit.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = LocalColors.current.primary
    )
}
