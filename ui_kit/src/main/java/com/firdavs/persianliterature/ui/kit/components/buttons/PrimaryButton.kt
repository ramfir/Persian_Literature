package com.firdavs.persianliterature.ui.kit.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        contentColor = Color.Black,
        containerColor = LocalColors.current.primary,
        disabledContentColor = Color.Black,
        disabledContainerColor = LocalColors.current.outline
    ),
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: TextUnit = TextUnit.Unspecified,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = buttonColors,
        onClick = onClick
    ) {
        H4Text(
            text = text,
            maxLines = maxLines,
            overflow = overflow,
            fontSize = fontSize
        )
    }
}
