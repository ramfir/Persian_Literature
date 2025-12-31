package com.firdavs.persianliterature.about_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.about_app.R
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
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
                    text = stringResource(UiR.string.about_app),
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                H3Text(
                    res = R.string.overview
                )
                T2Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    res = R.string.about_app_description
                )
                H3Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.features)
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.features) { feature ->
                        T2Text(
                            text = "• ${stringResource(feature)}"
                        )
                    }
                }
                H3Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.app_author)
                )
                T2Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    res = R.string.firdavs
                )
            }
        }
    )
}
