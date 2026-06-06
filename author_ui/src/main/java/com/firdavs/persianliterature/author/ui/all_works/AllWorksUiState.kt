package com.firdavs.persianliterature.author.ui.all_works

import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class AllWorksUiState(
    val groups: List<AuthorWorksGroup> = emptyList(),
    val isLoading: Boolean = true,
    val isSearchActive: Boolean = false,
    val searchQuery: String = "",
    val chapters: List<Chapter> = Chapter.all
) : UiState()
