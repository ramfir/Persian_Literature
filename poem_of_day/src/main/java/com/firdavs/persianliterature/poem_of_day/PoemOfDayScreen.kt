package com.firdavs.persianliterature.poem_of_day

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.firdavs.persianliterature.ui.kit.theme.localizedContext
import com.firdavs.persianliterature.ui.kit.theme.stringResource
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
            onNewPoemClick = viewModel::onNewPoemClick,
            onPreviousPoemClick = viewModel::onPreviousPoemClick,
            resetShowToastFlag = viewModel::resetShowToastFlag
        )
    }
}

@Composable
private fun PoemOfDayScreen(
    state: PoemOfDayUiState,
    onChapterClick: (Chapter) -> Unit,
    onNewPoemClick: () -> Unit,
    onPreviousPoemClick: () -> Unit,
    resetShowToastFlag: () -> Unit
) {
    val context = localizedContext()

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
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
                    text = stringResource(UiR.string.poem_of_day),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.previous_poem),
                            enabled = !state.isLoading && state.allPoems.isNotEmpty(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            onClick = onPreviousPoemClick
                        )
                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.new_poem),
                            enabled = !state.isLoading && state.allPoems.isNotEmpty(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            onClick = onNewPoemClick
                        )
                    }
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
