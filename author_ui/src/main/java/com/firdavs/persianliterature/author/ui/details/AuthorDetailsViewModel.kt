package com.firdavs.persianliterature.author.ui.details

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author.ui.mapper.toUi
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AuthorDetailsViewModel(
    private val id: String,
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val favouritesRepository: FavouritesRepository
) : BaseViewModel<AuthorDetailsUiState>(AuthorDetailsUiState(null)) {

    init {
        observeAuthor()
        observeWorks()
    }

    private fun observeAuthor() {
        viewModelScope.launch {
            authorRepository.getAuthor(id).collect { author ->
                post {
                    it.copy(author = author.toUi(), isLoading = false)
                }
            }
        }
    }

    private fun observeWorks() {
        viewModelScope.launch {
            worksRepository.getWorksByAuthorId(id).collect { works ->
                post { it.copy(works = works) }
            }
        }
    }

    fun onToggleAuthorFavourite(isFavourite: Boolean) {
        viewModelScope.launch {
            favouritesRepository.toggleAuthorFavourite(id, isFavourite)
        }
    }

    fun onToggleWorkFavourite(workId: String, isFavourite: Boolean) {
        viewModelScope.launch {
            favouritesRepository.toggleWorkFavourite(workId, isFavourite)
        }
    }
}
