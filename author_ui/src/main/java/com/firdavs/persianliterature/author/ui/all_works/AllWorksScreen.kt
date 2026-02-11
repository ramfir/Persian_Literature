package com.firdavs.persianliterature.author.ui.all_works

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.LocalTypography
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun AllWorksEntryPoint(
    onAuthorClick: (String) -> Unit,
    onWorkClick: (String) -> Unit,
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(AllWorksViewModel::class) { state, viewModel ->
        AllWorksScreen(
            state = state,
            onSearchClick = viewModel::onSearchClick,
            onExitSearchClick = viewModel::onExitSearchClick,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onClearSearchQueryClick = viewModel::onClearSearchQueryClick,
            applySearchFilter = viewModel::applySearchFilter,
            onAuthorClick = onAuthorClick,
            onWorkClick = onWorkClick,
            onChapterClick = onChapterClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllWorksScreen(
    state: AllWorksUiState,
    onSearchClick: () -> Unit,
    onExitSearchClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearchQueryClick: () -> Unit,
    applySearchFilter: () -> Unit,
    onAuthorClick: (String) -> Unit,
    onWorkClick: (String) -> Unit,
    onChapterClick: (Chapter) -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.AllWorks,
                onChapterClick = onChapterClick
            )
        },
        topBar = { drawerState, scope ->
            TopBar(
                drawerState = drawerState,
                scope = scope,
                isSearchActive = state.isSearchActive,
                searchQuery = state.searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onClearSearchQueryClick = onClearSearchQueryClick,
                onSearchClick = onSearchClick,
                onExitSearchClick = onExitSearchClick,
                applySearchFilter = applySearchFilter
            )
        },
        mainContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    ProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.items) { item ->
                            when (item) {
                                is AllWorksListItem.AuthorHeader -> {
                                    AuthorHeaderItem(
                                        authorName = item.authorName,
                                        authorId = item.authorId,
                                        onAuthorClick = onAuthorClick
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        thickness = 2.dp,
                                        color = LocalColors.current.primary
                                    )
                                }
                                is AllWorksListItem.WorkItem -> {
                                    WorkItem(
                                        work = item.work,
                                        onWorkClick = onWorkClick
                                    )
                                }
                                is AllWorksListItem.EmptyWorksMessage -> {
                                    H3Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        res = R.string.no_works_found,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun TopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    isSearchActive: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearchQueryClick: () -> Unit,
    onSearchClick: () -> Unit,
    onExitSearchClick: () -> Unit,
    applySearchFilter: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalColors.current.primary)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchActive) {
            IconButton(onClick = onExitSearchClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        } else {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(Icons.Default.Menu, "Open drawer")
            }
        }
        LaunchedEffect(searchQuery) {
            snapshotFlow {
                searchQuery
            }
                .debounce(SEARCH_DEBOUNCE)
                .collect {
                    applySearchFilter()
                }
        }
        if (isSearchActive) {
            LaunchedEffect(Unit) {
                if (isSearchActive) {
                    focusRequester.requestFocus()
                }
            }
            TextField(
                value = searchQuery,
                textStyle = LocalTypography.current.h3TextStyle,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .focusRequester(focusRequester),
                placeholder = {
                    H5Text(stringResource(R.string.search_all_works))
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    cursorColor = LocalColors.current.primary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = onClearSearchQueryClick) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Clear search"
                            )
                        }
                    }
                }
            )
        } else {
            Spacer(Modifier.weight(1f))
            H3Text(
                text = stringResource(com.firdavs.persianliterature.core.R.string.all_works),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.weight(1f))
            IconButton(onClick = onSearchClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
        }
    }
}

@Composable
private fun AuthorHeaderItem(
    authorName: String,
    authorId: String,
    onAuthorClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAuthorClick(authorId) }
            .background(LocalColors.current.primary.copy(alpha = 0.1f))
            .padding(16.dp)
    ) {
        H3Text(
            text = authorName,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun WorkItem(
    work: Work,
    onWorkClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onWorkClick(work.id)
            })
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        H4Text(text = work.title)
        work.publishYear?.let { year ->
            H5Text(text = stringResource(R.string.published_at, year))
        }
    }
}

private const val SEARCH_DEBOUNCE = 300L
