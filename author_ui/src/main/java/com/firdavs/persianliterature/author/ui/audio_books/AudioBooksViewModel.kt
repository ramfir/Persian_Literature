package com.firdavs.persianliterature.author.ui.audio_books

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AudioBooksViewModel(
    private val worksRepository: WorksRepository,
    private val authorRepository: AuthorRepository
) : BaseViewModel<AudioBooksUiState>(AudioBooksUiState()) {

    init {
        observeWorksWithAudio()
    }

    private fun observeWorksWithAudio() {
        viewModelScope.launch {
            combine(
                worksRepository.getWorksWithAudio(),
                authorRepository.getAuthors()
            ) { works, authors ->
                val authorsMap = authors.associate { it.id to it.name }
                works to authorsMap
            }.collect { (works, authorsMap) ->
                post { it.copy(works = works, authorsMap = authorsMap) }
            }
        }
    }
}
