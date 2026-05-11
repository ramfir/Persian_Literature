package com.firdavs.persianliterature.settings.ui.main

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.settings.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import kotlinx.coroutines.launch
import com.firdavs.persianliterature.core.R as UiR

@Composable
fun SettingsEntryPoint(
    onChapterClick: (Chapter) -> Unit,
    onChangeLanguageClick: () -> Unit
) {
    BaseEntryPoint(SettingsViewModel::class) { state, viewModel ->
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                viewModel.onNotificationToggle(true)
            }
        }

        SettingsScreen(
            state = state,
            onChapterClick = onChapterClick,
            onChangeLanguageClick = onChangeLanguageClick,
            onNotificationToggle = { enabled ->
                if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    viewModel.onNotificationToggle(enabled)
                }
            }
        )
    }
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onChapterClick: (Chapter) -> Unit,
    onChangeLanguageClick: () -> Unit,
    onNotificationToggle: (Boolean) -> Unit
) {
    val colors = LocalColors.current
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
                    text = stringResource(UiR.string.settings),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    onClick = onChangeLanguageClick
                ) {
                    H3Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = stringResource(R.string.change_language),
                        textAlign = TextAlign.Center
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            H3Text(text = stringResource(R.string.enable_notifications))
                            Text(
                                text = stringResource(R.string.notification_description),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Switch(
                            checked = state.notificationsEnabled,
                            onCheckedChange = onNotificationToggle,
                            colors = SwitchDefaults.colors().copy(
                                checkedThumbColor = colors.primary,
                                checkedTrackColor = colors.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    )
}
