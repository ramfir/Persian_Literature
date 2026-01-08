package com.firdavs.persianliterature.author.ui.audio_books

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun AudioBooksEntryPoint(
    onChapterClick: (Chapter) -> Unit,
    onWorkClick: (String) -> Unit
) {
    BaseEntryPoint(AudioBooksViewModel::class) { state, _ ->
        AudioBooksScreen(
            state = state,
            onChapterClick = onChapterClick,
            onWorkClick = onWorkClick
        )
    }
}

@Composable
private fun AudioBooksScreen(
    state: AudioBooksUiState,
    onChapterClick: (Chapter) -> Unit,
    onWorkClick: (String) -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.AudioBooks,
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
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(com.firdavs.persianliterature.core.R.string.audio_books),
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            AudioBooksContent(
                works = state.works,
                onWorkClick = onWorkClick
            )
        }
    )
}

@Composable
private fun AudioBooksContent(
    works: List<Work>,
    onWorkClick: (String) -> Unit
) {
    if (works.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            H4Text(
                text = stringResource(R.string.no_audio_books_found),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(works) { work ->
                AudioBookItem(
                    work = work,
                    onWorkClick = onWorkClick
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = LocalColors.current.primary
                )
            }
        }
    }
}

@Composable
private fun AudioBookItem(
    work: Work,
    onWorkClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onWorkClick(work.id) }
            .padding(horizontal = 16.dp)
    ) {
        H4Text(text = work.title)
        work.publishYear?.let { year ->
            H5Text(text = stringResource(R.string.published_at, year))
        }
    }
}
