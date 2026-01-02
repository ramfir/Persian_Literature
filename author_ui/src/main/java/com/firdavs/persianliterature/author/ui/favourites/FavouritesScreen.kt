package com.firdavs.persianliterature.author.ui.favourites

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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Person
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
import com.firdavs.persianliterature.author.ui.list.AuthorItem
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
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
fun FavouritesEntryPoint(
    onChapterClick: (Chapter) -> Unit,
    onAuthorClick: (String) -> Unit,
    onWorkClick: (String) -> Unit
) {
    BaseEntryPoint(FavouritesViewModel::class) { state, viewModel ->
        FavouritesScreen(
            state = state,
            onChapterClick = onChapterClick,
            onTabSelected = viewModel::onTabSelected,
            onAuthorClick = onAuthorClick,
            onWorkClick = onWorkClick,
            onRemoveAuthorFromFavourites = viewModel::onRemoveAuthorFromFavourites,
            onRemoveWorkFromFavourites = viewModel::onRemoveWorkFromFavourites
        )
    }
}

@Composable
private fun FavouritesScreen(
    state: FavouritesUiState,
    onChapterClick: (Chapter) -> Unit,
    onTabSelected: (FavouritesTab) -> Unit,
    onAuthorClick: (String) -> Unit,
    onWorkClick: (String) -> Unit,
    onRemoveAuthorFromFavourites: (String) -> Unit,
    onRemoveWorkFromFavourites: (String) -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.Favourites,
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
                    text = stringResource(R.string.favourites),
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when (state.selectedTab) {
                    FavouritesTab.Authors -> {
                        FavouriteAuthorsContent(
                            authors = state.favouriteAuthors,
                            onAuthorClick = onAuthorClick,
                            onRemoveFromFavourites = onRemoveAuthorFromFavourites
                        )
                    }
                    FavouritesTab.Works -> {
                        FavouriteWorksContent(
                            works = state.favouriteWorks,
                            onWorkClick = onWorkClick,
                            onRemoveFromFavourites = onRemoveWorkFromFavourites
                        )
                    }
                }
            }
        },
        footerContent = {
            FavouritesFooter(
                selectedTab = state.selectedTab,
                onTabSelected = onTabSelected
            )
        }
    )
}

@Composable
private fun FavouriteAuthorsContent(
    authors: List<AuthorUiModel>,
    onAuthorClick: (String) -> Unit,
    onRemoveFromFavourites: (String) -> Unit
) {
    if (authors.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            H4Text(
                text = stringResource(R.string.no_favourite_authors),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(authors) { author ->
                FavouriteAuthorItem(
                    author = author,
                    onAuthorClick = onAuthorClick,
                    onRemoveFromFavourites = onRemoveFromFavourites
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
private fun FavouriteAuthorItem(
    author: AuthorUiModel,
    onAuthorClick: (String) -> Unit,
    onRemoveFromFavourites: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAuthorClick(author.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            AuthorItem(author = author, onAuthorClick = onAuthorClick)
        }
        IconButton(
            onClick = { onRemoveFromFavourites(author.id) }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Remove from favourites",
                tint = LocalColors.current.primary
            )
        }
    }
}

@Composable
private fun FavouriteWorksContent(
    works: List<Work>,
    onWorkClick: (String) -> Unit,
    onRemoveFromFavourites: (String) -> Unit
) {
    if (works.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            H4Text(
                text = stringResource(R.string.no_favourite_works),
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
                FavouriteWorkItem(
                    work = work,
                    onWorkClick = onWorkClick,
                    onRemoveFromFavourites = onRemoveFromFavourites
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
private fun FavouriteWorkItem(
    work: Work,
    onWorkClick: (String) -> Unit,
    onRemoveFromFavourites: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onWorkClick(work.id) }
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            H4Text(text = work.title)
            H5Text(text = stringResource(R.string.published_at, work.publishYear))
        }
        IconButton(
            onClick = { onRemoveFromFavourites(work.id) }
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Remove from favourites",
                tint = LocalColors.current.primary
            )
        }
    }
}

@Composable
private fun FavouritesFooter(
    selectedTab: FavouritesTab,
    onTabSelected: (FavouritesTab) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(FavouritesTab.Authors) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Authors",
                tint = if (selectedTab == FavouritesTab.Authors) {
                    LocalColors.current.onPrimary
                } else {
                    LocalColors.current.onPrimary.copy(alpha = 0.5f)
                }
            )
        }
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = { onTabSelected(FavouritesTab.Works) }
        ) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = "Works",
                tint = if (selectedTab == FavouritesTab.Works) {
                    LocalColors.current.onPrimary
                } else {
                    LocalColors.current.onPrimary.copy(alpha = 0.5f)
                }
            )
        }
    }
}
