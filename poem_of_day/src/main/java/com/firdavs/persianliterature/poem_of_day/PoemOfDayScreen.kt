package com.firdavs.persianliterature.poem_of_day

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.components.buttons.PrimaryButton
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import kotlinx.coroutines.launch
import com.firdavs.persianliterature.core.R as UiR

@Composable
fun PoemOfDayEntryPoint(
    poemId: String?,
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(PoemOfDayViewModel::class, poemId) { state, viewModel ->
        PoemOfDayScreen(
            state = state,
            onChapterClick = onChapterClick,
            onRefreshClick = viewModel::onRefreshClick,
            onNewPoemClick = viewModel::onNewPoemClick,
            resetShowToastFlag = viewModel::resetShowToastFlag
        )
    }
}

@Composable
private fun PoemOfDayScreen(
    state: PoemOfDayUiState,
    onChapterClick: (Chapter) -> Unit,
    onRefreshClick: () -> Unit,
    onNewPoemClick: () -> Unit,
    resetShowToastFlag: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(state.showToast) {
        if (state.showToast) {
            Toast.makeText(
                context,
                context.getString(R.string.poems_refreshed),
                Toast.LENGTH_SHORT
            ).show()
            resetShowToastFlag()
        }
    }

    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.PoemOfDay,
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
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(UiR.string.poem_of_day),
                    textAlign = TextAlign.Center
                )
                if (!state.isRefreshing) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = onRefreshClick
                    ) {
                        Icon(Icons.Default.Refresh, "Refresh poems")
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(4.dp),
                            color = LocalColors.current.onPrimary
                        )
                    }
                }
            }
        },
        mainContent = {
            if (state.isLoading && state.poem == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.poem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LocalColors.current.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            H2Text(
                                text = state.poem.title,
                                textAlign = TextAlign.Center,
                                color = LocalColors.current.primary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            T1Text(
                                text = state.poem.text,
                                textAlign = TextAlign.Center,
                                color = LocalColors.current.onSurface
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            T2Text(
                                text = "— ${state.poem.author}",
                                textAlign = TextAlign.End,
                                color = LocalColors.current.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    PrimaryButton(
                        text = stringResource(R.string.new_poem),
                        enabled = !state.isLoading,
                        onClick = onNewPoemClick
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    T1Text(
                        text = stringResource(R.string.no_poems_available),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}
