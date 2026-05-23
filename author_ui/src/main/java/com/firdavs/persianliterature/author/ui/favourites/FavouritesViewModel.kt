package com.firdavs.persianliterature.author.ui.favourites

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val favouritesRepository: FavouritesRepository,
    private val authorUiMapper: AuthorUiMapper
) : BaseViewModel<FavouritesUiState>(FavouritesUiState()) {

    init {
        observeFavouriteAuthors()
        observeFavouriteWorks()
        observeFavouritePoems()
    }

    private fun observeFavouriteAuthors() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteAuthors().collect { authors ->
                post { it.copy(favouriteAuthors = authorUiMapper.map(authors)) }
            }
        }
    }

    private fun observeFavouriteWorks() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteWorks().collect { works ->
                post { it.copy(favouriteWorks = works) }
            }
        }
    }

    private fun observeFavouritePoems() {
        viewModelScope.launch {
            favouritesRepository.getFavouritePoems().collect { poems ->
                post { it.copy(favouritePoems = poems) }
            }
        }
    }

    fun onTabSelected(tab: FavouritesTab) {
        post { it.copy(selectedTab = tab) }
    }

    fun onRequestRemoveAuthor(authorId: String) {
        post { it.copy(pendingRemoval = PendingRemoval.Author(authorId)) }
    }

    fun onRequestRemoveWork(workId: String) {
        post { it.copy(pendingRemoval = PendingRemoval.Work(workId)) }
    }

    fun onRequestRemovePoem(poemId: String) {
        post { it.copy(pendingRemoval = PendingRemoval.Poem(poemId)) }
    }

    fun onConfirmRemoval() {
        val pending = state.value.pendingRemoval ?: return
        post { it.copy(pendingRemoval = null) }
        viewModelScope.launch {
            when (pending) {
                is PendingRemoval.Author -> favouritesRepository.toggleAuthorFavourite(pending.id, false)
                is PendingRemoval.Work -> favouritesRepository.toggleWorkFavourite(pending.id, false)
                is PendingRemoval.Poem -> favouritesRepository.togglePoemFavourite(pending.id, false)
            }
        }
    }

    fun onDismissRemoval() {
        post { it.copy(pendingRemoval = null) }
    }
}
