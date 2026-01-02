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

    fun onTabSelected(tab: FavouritesTab) {
        post { it.copy(selectedTab = tab) }
    }

    fun onRemoveAuthorFromFavourites(authorId: String) {
        viewModelScope.launch {
            favouritesRepository.toggleAuthorFavourite(authorId, false)
        }
    }

    fun onRemoveWorkFromFavourites(workId: String) {
        viewModelScope.launch {
            favouritesRepository.toggleWorkFavourite(workId, false)
        }
    }
}
