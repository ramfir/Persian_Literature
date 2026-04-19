package com.firdavs.persianliterature.author.ui.details

import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.presentation.UiState

data class AuthorDetailsUiState(
    val id: String?,
    val isLoading: Boolean = true,
    val author: AuthorUiModel? = null,
    val works: List<Work> = emptyList()
) : UiState()
