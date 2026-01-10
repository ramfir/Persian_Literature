package com.firdavs.persianliterature.author.ui.audio_books

import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.core.presentation.UiState

data class AudioBooksUiState(
    val works: List<Work> = emptyList(),
    val chapters: List<Chapter> = Chapter.all
) : UiState()
