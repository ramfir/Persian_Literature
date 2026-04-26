package com.firdavs.persianliterature.author.ui.list

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AuthorsListViewModel(
    private val authorRepository: AuthorRepository,
    private val authorUiMapper: AuthorUiMapper,
    private val favouritesRepository: FavouritesRepository
) :
    BaseViewModel<AuthorsListUiState>(AuthorsListUiState()) {
    init {
        observeAuthors()
    }

    private var allAuthors: List<AuthorUiModel> = emptyList()

    private fun observeAuthors() {
        viewModelScope.launch {
            var firstEmission = true
            authorRepository.getAuthors().collect { authors ->
                allAuthors = authorUiMapper.map(authors)
                // Keep loading if the first DB emission is empty (first launch, data not yet fetched from server).
                val stillLoading = firstEmission && authors.isEmpty()
                post { it.copy(authors = allAuthors, isLoading = stillLoading) }
                firstEmission = false
            }
        }
    }

    fun onSearchClick() {
        post { it.copy(isSearchActive = true) }
    }

    fun onExitSearchClick() {
        post { it.copy(isSearchActive = false) }
        onClearSearchQueryClick()
    }

    fun onSearchQueryChange(value: String) {
        post { it.copy(searchQuery = value) }
    }

    fun onClearSearchQueryClick() {
        post { it.copy(searchQuery = "") }
    }

    fun filterAuthorsList() {
        val searchQuery = state.value.searchQuery
        val filteredAuthors = allAuthors.filter { author ->
            author.name.contains(searchQuery, ignoreCase = true)
        }
        post { it.copy(authors = filteredAuthors) }
    }

    fun onToggleFavourite(authorId: String, isFavourite: Boolean) {
        viewModelScope.launch {
            favouritesRepository.toggleAuthorFavourite(authorId, isFavourite)
        }
    }
}
