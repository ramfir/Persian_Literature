package com.firdavs.persianliterature.ui.kit

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.firdavs.persianliterature.ui.kit.theme.AppTheme

@Composable
fun H2Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h2TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H3Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h3TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H4Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h4TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H5Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h5TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H5BoldText(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h5BoldTextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H6Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h6TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun T1Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.t1TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun T2Text(text: String, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.t2TextStyle.asText(text, modifier, color, textAlign)

@Composable
fun H2Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h2TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun H3Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h3TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun H4Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h4TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun H5Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h5TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun H5BoldText(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h5BoldTextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun H6Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.h6TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun T1Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.t1TextStyle.asText(stringResource(res), modifier, color, textAlign)

@Composable
fun T2Text(@StringRes res: Int, modifier: Modifier = Modifier, color: Color? = null, textAlign: TextAlign? = null) =
    AppTheme.typography.t2TextStyle.asText(stringResource(res), modifier, color, textAlign)

private fun TextStyle.applyIfNotNull(color: Color?) = run {
    if (color != null) copy(color = color) else this
}

@Composable
private fun TextStyle.asText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) = Text(
    text = text,
    modifier = modifier,
    style = this.applyIfNotNull(color),
    textAlign = textAlign
)
