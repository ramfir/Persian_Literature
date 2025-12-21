package com.firdavs.persianliterature.author.ui.details

import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.presentation.UiState
import java.io.File

data class AuthorDetailsUiState(
    val id: String?,
    val isLoading: Boolean = true,
    val author: AuthorUiModel? = null,
    val chapter: Chapter = Chapter.Bio,
    val isLoadingFile: Boolean = true,
    val bioFile: File? = null,
    val works: List<Work> = emptyList()
) : UiState()

enum class Chapter {
    Bio, Works
}

fun Chapter.getStringRes(): Int = when (this) {
    Chapter.Bio -> R.string.bio
    Chapter.Works -> R.string.works
}
