package com.firdavs.persianliterature.author.ui.favourites

import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class FavouritesUiState(
    val favouriteAuthors: List<AuthorUiModel> = emptyList(),
    val favouriteWorks: List<Work> = emptyList(),
    val selectedTab: FavouritesTab = FavouritesTab.Authors,
    val chapters: List<Chapter> = listOf(
        Chapter.Authors,
        Chapter.AboutApp,
        Chapter.Favourites,
        Chapter.Settings
    )
) : UiState()

enum class FavouritesTab {
    Authors, Works
}
