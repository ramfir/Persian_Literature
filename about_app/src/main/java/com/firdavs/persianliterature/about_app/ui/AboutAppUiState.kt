package com.firdavs.persianliterature.about_app.ui

import com.firdavs.persianliterature.about_app.R
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class AboutAppUiState(
    val chapters: List<Chapter> = listOf(
        Chapter.Authors,
        Chapter.AboutApp,
        Chapter.Favourites,
        Chapter.Settings
    ),
    val features: List<Int> = listOf(
        R.string.authors_list_with_works,
        R.string.add_favourites,
        R.string.languages
    )
) : UiState()
