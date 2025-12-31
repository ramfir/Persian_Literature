package com.firdavs.persianliterature.author.ui.list

import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class AuthorsListUiState(
    val authors: List<AuthorUiModel> = emptyList(),
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val chapters: List<Chapter> = listOf(
        Chapter.AboutApp,
        Chapter.Favourites,
        Chapter.Settings
    )
) : UiState()
