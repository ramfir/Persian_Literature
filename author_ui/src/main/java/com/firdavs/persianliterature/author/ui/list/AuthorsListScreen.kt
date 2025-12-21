package com.firdavs.persianliterature.author.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.theme.AppPreviewTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.LocalTypography
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@Composable
fun AuthorsListEntryPoint(
    onAuthorClick: (String) -> Unit
) {
    BaseEntryPoint(AuthorsListViewModel::class) { state, viewModel ->
        AuthorsListScreen(
            state = state,
            onSearchClick = viewModel::onSearchClick,
            onExitSearchClick = viewModel::onExitSearchClick,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onClearSearchQueryClick = viewModel::onClearSearchQueryClick,
            onAuthorClick = onAuthorClick,
            filterAuthorsList = viewModel::filterAuthorsList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthorsListScreen(
    state: AuthorsListUiState,
    onSearchClick: () -> Unit,
    onExitSearchClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onClearSearchQueryClick: () -> Unit,
    onAuthorClick: (String) -> Unit,
    filterAuthorsList: () -> Unit
) {
    BaseScreen(
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        stringResource(R.string.favourites),
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        stringResource(R.string.settings),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
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
                filterAuthorsList = filterAuthorsList
            )
        },
        mainContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.authors) { author ->
                            AuthorItem(author, onAuthorClick)
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = LocalColors.current.primary
                            )
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
    filterAuthorsList: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
        if (isSearchActive) {
            LaunchedEffect(searchQuery) {
                snapshotFlow {
                    searchQuery
                }
                    .debounce(SEARCH_DEBOUNCE)
                    .collect {
                        filterAuthorsList()
                    }
            }
            TextField(
                value = searchQuery,
                textStyle = LocalTypography.current.h3TextStyle,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .padding(start = 32.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                placeholder = {
                    H3Text(stringResource(R.string.search_authors))
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
            H3Text(text = stringResource(R.string.authors_list))
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
fun AuthorItem(
    author: AuthorUiModel,
    onAuthorClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAuthorClick(author.id) }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // for preview
        if (LocalInspectionMode.current) {
            Image(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                painter = painterResource(R.drawable.img_rudaki),
                contentDescription = null
            )
        } else {
            GlideImage(
                imageModel = { author.photoUrl },
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                    )
                },
                failure = {
                    Image(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        painter = painterResource(R.drawable.img_rudaki),
                        contentDescription = null
                    )
                }
            )
        }
        T1Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = "${author.birthDate} - ${author.deathDate}"
        )
        H3Text(
            modifier = Modifier
                .padding(top = 4.dp),
            text = author.fullName
        )
    }
}

@Preview
@Composable
private fun AuthorsListScreenPreview(
    @PreviewParameter(AuthorsListStateProvider ::class) state: AuthorsListUiState
) {
    AppPreviewTheme {
        AuthorsListScreen(
            state = state,
            onSearchClick = {},
            onExitSearchClick = {},
            onSearchQueryChange = {},
            onClearSearchQueryClick = {},
            onAuthorClick = {},
            filterAuthorsList = {}
        )
    }
}

private const val SEARCH_DEBOUNCE = 300L
