package com.firdavs.persianliterature.author_ui.list

import com.firdavs.persianliterature.author_ui.model.AuthorUiModel
import com.firdavs.persianliterature.core.presentation.UiState

data class AuthorsListUiState(
    val authors: List<AuthorUiModel> = emptyList(),
    val isSearchActive: Boolean = false,
    val searchQuery: String = ""
    ) : UiState()
