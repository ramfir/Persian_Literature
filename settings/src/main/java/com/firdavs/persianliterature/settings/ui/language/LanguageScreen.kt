package com.firdavs.persianliterature.settings.ui.language

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.settings.R
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.theme.localizedContext
import com.firdavs.persianliterature.ui.kit.components.buttons.PrimaryButton
import com.firdavs.persianliterature.ui.kit.theme.LocalAppLocale
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import java.util.Locale

@Composable
fun LanguageEntryPoint(
    onBackClick: () -> Unit
) {
    BaseEntryPoint(LanguageViewModel::class) { state, viewModel ->
        // Update Locale.setDefault when language changes for immediate effect
        val currentLocale = LocalAppLocale.current
        LaunchedEffect(currentLocale) {
            Locale.setDefault(currentLocale)
        }

        LanguageScreen(
            state = state,
            onBackClick = onBackClick,
            onLanguageSelected = viewModel::onLanguageSelected,
            onApplyClick = viewModel::onApplyClick,
            resetShowErrorToastFlag = viewModel::resetShowErrorToastFlag,
            resetShowSuccessToastFlag = viewModel::resetShowSuccessToastFlag
        )
    }
}

@Composable
private fun LanguageScreen(
    state: LanguageUiState,
    onBackClick: () -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onApplyClick: () -> Unit,
    resetShowErrorToastFlag: () -> Unit,
    resetShowSuccessToastFlag: () -> Unit
) {
    val context = localizedContext()
    val colors = LocalColors.current

    LaunchedEffect(state.showErrorToast) {
        if (state.showErrorToast) {
            Toast.makeText(
                context,
                R.string.language_change_error,
                Toast.LENGTH_LONG
            ).show()
            resetShowErrorToastFlag()
        }
    }

    LaunchedEffect(state.showSuccessToast) {
        if (state.showSuccessToast) {
            Toast.makeText(
                context,
                R.string.language_change_success,
                Toast.LENGTH_SHORT
            ).show()
            resetShowSuccessToastFlag()
        }
    }

    BaseScreen(
        topBar = { _, _ ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.current.primary)
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
                H3Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(R.string.select_language),
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                H3Text(
                    text = stringResource(R.string.select_language)
                )

                Language.entries.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onLanguageSelected(language) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.selectedLanguage == language,
                            onClick = null,
                            colors = RadioButtonDefaults.colors().copy(
                                selectedColor = colors.primary
                            )
                        )
                        H3Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = language.displayName
                        )
                    }
                }
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    text = stringResource(R.string.apply),
                    onClick = onApplyClick
                )
            }
        }
    )
}
