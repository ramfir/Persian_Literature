package com.firdavs.persianliterature.author.ui.audio_books

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AudioBooksViewModel(
    private val worksRepository: WorksRepository
) : BaseViewModel<AudioBooksUiState>(AudioBooksUiState()) {

    init {
        observeWorksWithAudio()
    }

    private fun observeWorksWithAudio() {
        viewModelScope.launch {
            worksRepository.getWorksWithAudio().collect { works ->
                post { it.copy(works = works) }
            }
        }
    }
}
