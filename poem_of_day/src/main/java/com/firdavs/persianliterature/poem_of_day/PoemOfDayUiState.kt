package com.firdavs.persianliterature.poem_of_day

import com.firdavs.persianliterature.author_api.model.Poem
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class PoemOfDayUiState(
    val poem: Poem? = null,
    val allPoems: List<Poem> = emptyList(),
    val currentIndex: Int = -1,
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val showToast: Boolean = false,
    val chapters: List<Chapter> = Chapter.all
) : UiState()
