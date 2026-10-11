package com.firdavs.persianliterature.suggest_poem

import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.components.buttons.PrimaryButton
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import kotlinx.coroutines.launch
import com.firdavs.persianliterature.core.R as CoreR

@Composable
fun SuggestPoemEntryPoint(
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(SuggestPoemViewModel::class) { state, viewModel ->
        SuggestPoemScreen(
            state = state,
            onChapterClick = onChapterClick,
            onTitleChange = viewModel::onTitleChange,
            onAuthorChange = viewModel::onAuthorChange,
            onTextChange = viewModel::onTextChange,
            onSuggesterNameChange = viewModel::onSuggesterNameChange,
            onSubmitClick = viewModel::onSubmitClick,
            onSendAnotherClick = viewModel::onSendAnotherClick
        )
    }
}

@Suppress("LongMethod")
@Composable
private fun SuggestPoemScreen(
    state: SuggestPoemUiState,
    onChapterClick: (Chapter) -> Unit,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onSuggesterNameChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onSendAnotherClick: () -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.SuggestPoem,
                onChapterClick = onChapterClick
            )
        },
        topBar = { drawerState, scope ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.current.primary)
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } }
                ) {
                    Icon(Icons.Default.Menu, "Open drawer")
                }
                H3Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
                    text = stringResource(CoreR.string.suggest_poem),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        mainContent = {
            if (state.isSubmitted) {
                SuccessContent(onSendAnotherClick = onSendAnotherClick)
            } else {
                FormContent(
                    state = state,
                    onTitleChange = onTitleChange,
                    onAuthorChange = onAuthorChange,
                    onTextChange = onTextChange,
                    onSuggesterNameChange = onSuggesterNameChange,
                    onSubmitClick = onSubmitClick
                )
            }
        }
    )
}

@Suppress("LongMethod")
@Composable
private fun FormContent(
    state: SuggestPoemUiState,
    onTitleChange: (String) -> Unit,
    onAuthorChange: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onSuggesterNameChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    val colors = LocalColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        T1Text(
            text = stringResource(R.string.suggest_poem_hint)
        )
        PoemTextField(
            value = state.title,
            onValueChange = onTitleChange,
            label = stringResource(R.string.suggest_poem_title_field),
            enabled = !state.isSubmitting
        )
        PoemTextField(
            value = state.author,
            onValueChange = onAuthorChange,
            label = stringResource(R.string.suggest_poem_author_field),
            enabled = !state.isSubmitting
        )
        PoemTextField(
            value = state.text,
            onValueChange = onTextChange,
            label = stringResource(R.string.suggest_poem_text_field),
            enabled = !state.isSubmitting,
            singleLine = false,
            modifier = Modifier.heightIn(min = 160.dp)
        )
        PoemTextField(
            value = state.suggesterName,
            onValueChange = onSuggesterNameChange,
            label = stringResource(R.string.suggest_poem_name_field),
            enabled = !state.isSubmitting,
            imeAction = ImeAction.Done
        )
        if (state.errorRes != null) {
            T2Text(
                text = stringResource(state.errorRes),
                color = colors.error
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (state.isSubmitting) {
                ProgressIndicator()
            } else {
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.suggest_poem_submit),
                    onClick = onSubmitClick
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun PoemTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Next
) {
    val colors = LocalColors.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        label = { H5Text(text = label) },
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(
            // A multiline field keeps Enter as a line break instead of an IME action
            imeAction = if (singleLine) imeAction else ImeAction.Default
        ),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = colors.primary,
            focusedBorderColor = colors.primary,
            unfocusedBorderColor = colors.primary,
            disabledBorderColor = colors.primary,
            focusedLabelColor = colors.primary
        )
    )
}

@Composable
private fun SuccessContent(
    onSendAnotherClick: () -> Unit
) {
    val colors = LocalColors.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        H2Text(
            text = stringResource(R.string.suggest_poem_success_title),
            color = colors.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        T1Text(
            text = stringResource(R.string.suggest_poem_success_message),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(R.string.suggest_poem_send_another),
            onClick = onSendAnotherClick
        )
    }
}
