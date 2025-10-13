package com.firdavs.persianliterature.author_ui.list

import com.firdavs.persianliterature.core.presentation.UiState

data class AuthorsListUiState(
    val authors: List<Int>
) : UiState()
