package com.firdavs.persianliterature.suggest_poem

import androidx.annotation.StringRes
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class SuggestPoemUiState(
    val title: String = "",
    val author: String = "",
    val text: String = "",
    val suggesterName: String = "",
    val isSubmitting: Boolean = false,
    val isSubmitted: Boolean = false,
    @StringRes val errorRes: Int? = null,
    val chapters: List<Chapter> = Chapter.all
) : UiState()
