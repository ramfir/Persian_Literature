package com.firdavs.persianliterature.ui.kit

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.stringResource

@Composable
fun H2Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = AppTheme.typography.h2TextStyle.asText(text, modifier, color, textAlign, maxLines, overflow)

@Composable
fun H3Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = AppTheme.typography.h3TextStyle.asText(text, modifier, color, textAlign, maxLines, overflow)

@Composable
fun H4Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified
) =
    AppTheme.typography.h4TextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        maxLines,
        overflow,
        fontSize
    )

@Composable
fun H5Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h5TextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun H5BoldText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h5BoldTextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun H6Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h6TextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun T1Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.t1TextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun T2Text(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.t2TextStyle.asText(
        text,
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun H2Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = AppTheme.typography.h2TextStyle.asText(
    stringResource(res),
    modifier,
    color,
    textAlign,
    maxLines,
    overflow
)

@Composable
fun H3Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) = AppTheme.typography.h3TextStyle.asText(
    stringResource(res),
    modifier,
    color,
    textAlign,
    maxLines,
    overflow
)

@Composable
fun H4Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) =
    AppTheme.typography.h4TextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        maxLines,
        overflow
    )

@Composable
fun H5Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h5TextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun H5BoldText(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h5BoldTextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun H6Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.h6TextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun T1Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.t1TextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

@Composable
fun T2Text(
    @StringRes res: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null
) =
    AppTheme.typography.t2TextStyle.asText(
        stringResource(res),
        modifier,
        color,
        textAlign,
        Int.MAX_VALUE,
        TextOverflow.Clip
    )

private fun TextStyle.applyIfNotNull(color: Color?) = run {
    if (color != null) copy(color = color) else this
}

@Composable
private fun TextStyle.asText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    fontSize: androidx.compose.ui.unit.TextUnit = androidx.compose.ui.unit.TextUnit.Unspecified
) = Text(
    text = text,
    modifier = modifier,
    style = this.applyIfNotNull(color).let {
        if (fontSize != androidx.compose.ui.unit.TextUnit.Unspecified) it.copy(fontSize = fontSize) else it
    },
    textAlign = textAlign,
    maxLines = maxLines,
    overflow = overflow
)
