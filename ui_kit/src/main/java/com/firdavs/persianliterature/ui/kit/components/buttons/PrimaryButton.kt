package com.firdavs.persianliterature.ui.kit.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = LocalColors.current.onPrimary,
        containerColor = LocalColors.current.primary,
        disabledContentColor = LocalColors.current.onPrimary,
        disabledContainerColor = LocalColors.current.outline
    ),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = buttonColors,
        onClick = onClick,
    ) {
        H3Text(text = text)
    }
}
