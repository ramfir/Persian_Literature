package com.firdavs.persianliterature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import kotlinx.coroutines.launch
import com.firdavs.persianliterature.core.R as UiR

@Composable
fun SettingsEntryPoint(
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(SettingsViewModel::class) { state, viewModel ->
        SettingsScreen(
            state = state,
            onChapterClick = onChapterClick,
            onLanguageSelected = viewModel::onLanguageSelected,
            onApplyClick = viewModel::onApplyClick
        )
    }
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onChapterClick: (Chapter) -> Unit,
    onLanguageSelected: (Language) -> Unit,
    onApplyClick: (Language) -> Unit
) {
    val context = LocalContext.current

    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.Settings,
                onChapterClick = onChapterClick
            )
        },
        topBar = { drawerState, scope ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } }
                ) {
                    Icon(Icons.Default.Menu, "Open drawer")
                }
                H3Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = stringResource(UiR.string.settings),
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

                Language.values().forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.selectedLanguage == language,
                            onClick = { onLanguageSelected(language) }
                        )
                        H3Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = language.displayName
                        )
                    }
                }

                Button(
                    onClick = { onApplyClick(state.selectedLanguage) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    H3Text(text = stringResource(R.string.apply))
                }
            }
        }
    )
}
