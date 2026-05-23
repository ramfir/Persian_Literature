package com.firdavs.persianliterature.author.ui.favourites

import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Poem
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class FavouritesUiState(
    val favouriteAuthors: List<AuthorUiModel> = emptyList(),
    val favouriteWorks: List<Work> = emptyList(),
    val favouritePoems: List<Poem> = emptyList(),
    val selectedTab: FavouritesTab = FavouritesTab.Authors,
    val chapters: List<Chapter> = Chapter.all,
    val pendingRemoval: PendingRemoval? = null
) : UiState()

enum class FavouritesTab {
    Authors, Works, Poems
}

sealed class PendingRemoval {
    data class Author(val id: String) : PendingRemoval()
    data class Work(val id: String) : PendingRemoval()
    data class Poem(val id: String) : PendingRemoval()
}
