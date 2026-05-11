package com.firdavs.persianliterature.author.ui.details

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.author.ui.list.AuthorItem
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.theme.AppPreviewTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun AuthorDetailsEntryPoint(
    id: String,
    onBackClick: () -> Unit,
    onWorkClick: (String) -> Unit
) {
    BaseEntryPoint(AuthorDetailsViewModel::class, id) { state, viewModel ->
        AuthorDetailsScreen(
            state = state,
            onWorkClick = onWorkClick,
            onBackClick = onBackClick,
            onToggleAuthorFavourite = viewModel::onToggleAuthorFavourite,
            onToggleWorkFavourite = viewModel::onToggleWorkFavourite
        )
    }
}

@Composable
fun AuthorDetailsScreen(
    state: AuthorDetailsUiState,
    onWorkClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onToggleAuthorFavourite: (Boolean) -> Unit = {},
    onToggleWorkFavourite: (String, Boolean) -> Unit = { _, _ -> }
) {
    BaseScreen(
        topBar = { _, _ ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.current.primary)
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                H2Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
                    res = R.string.author,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                state.author?.let { author ->
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        onClick = { onToggleAuthorFavourite(!author.isFavourite) }
                    ) {
                        Icon(
                            imageVector = if (author.isFavourite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = null,
                            tint = LocalColors.current.onPrimary
                        )
                    }
                }
            }
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
                } else if (state.author != null) {
                    AuthorWithWorksContent(
                        author = state.author,
                        works = state.works,
                        onWorkClick = onWorkClick,
                        onToggleWorkFavourite = onToggleWorkFavourite
                    )
                }
            }
        }
    )
}

@Composable
private fun AuthorWithWorksContent(
    author: AuthorUiModel,
    works: List<Work>,
    onWorkClick: (String) -> Unit,
    onToggleWorkFavourite: (String, Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        if (works.isEmpty()) {
            AuthorItem(
                author = author,
                onAuthorClick = {}
            )
            Spacer(Modifier.weight(1f))
            H3Text(
                modifier = Modifier
                    .fillMaxWidth(),
                res = R.string.no_works_found,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = CenterHorizontally
            ) {
                item {
                    AuthorItem(
                        author = author,
                        onAuthorClick = {}
                    )
                }
                items(works) { work ->
                    WorkItem(
                        work = work,
                        onWorkClick = onWorkClick,
                        onToggleFavourite = onToggleWorkFavourite
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkItem(
    work: Work,
    onWorkClick: (String) -> Unit,
    onToggleFavourite: (String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = com.firdavs.persianliterature.ui.kit.theme.AppTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.current.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = {
                    onWorkClick(work.id)
                })
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                H4Text(text = work.title)
                work.publishYear?.let { year ->
                    H5Text(text = stringResource(R.string.published_at, year))
                }
            }
            IconButton(
                onClick = { onToggleFavourite(work.id, !work.isFavourite) }
            ) {
                Icon(
                    imageVector = if (work.isFavourite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = LocalColors.current.primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun AuthorDetailsScreenPreview(
    @PreviewParameter(AuthorDetailsStateProvider::class) state: AuthorDetailsUiState
) {
    AppPreviewTheme {
        AuthorDetailsScreen(
            state = state,
            onWorkClick = {},
            onBackClick = {}
        )
    }
}
