package com.firdavs.persianliterature.about_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.about_app.R
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import kotlinx.coroutines.launch
import com.firdavs.persianliterature.core.R as UiR

@Composable
fun AboutAppEntryPoint(
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(AboutAppViewModel::class) { state, viewModel ->
        AboutAppScreen(
            state = state,
            onChapterClick = onChapterClick
        )
    }
}

@Composable
private fun AboutAppScreen(
    state: AboutAppUiState,
    onChapterClick: (Chapter) -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.AboutApp,
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
                    text = stringResource(UiR.string.about_app),
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
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                H3Text(
                    res = R.string.overview
                )
                H5Text(
                    res = R.string.about_app_description
                )
                H3Text(
                    text = stringResource(R.string.features)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.features.forEach { feature ->
                        H5Text(
                            text = "• ${stringResource(feature)}"
                        )
                    }
                }
                H3Text(
                    text = stringResource(R.string.app_author)
                )
                H5Text(
                    res = R.string.firdavs
                )
            }
        }
    )
}
