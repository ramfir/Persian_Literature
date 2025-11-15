package com.firdavs.persianliterature.author.ui.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AuthorsListViewModel(
    private val authorRepository: AuthorRepository,
    private val authorUiMapper: AuthorUiMapper
) :
    BaseViewModel<AuthorsListUiState>(AuthorsListUiState()) {
    init {
        observeAuthors()
        fetchAuthors()
    }

    private fun observeAuthors() {
        viewModelScope.launch {
            authorRepository.getAuthors().collect { authors ->
                post { it.copy(authors = authorUiMapper.map(authors)) }
            }
        }
    }

    private fun fetchAuthors() {
        post { it.copy(isLoading = true) }
        viewModelScope.launch {
            runCatching {
                authorRepository.fetchAuthors()
            }.onFailure {
                Log.e(TAG, "getAuthors error ", it)
                post { it.copy(isLoading = false) }
            }.onSuccess {
                post { it.copy(isLoading = false) }
            }
        }
    }

    fun onSearchClick() {
        post { it.copy(isSearchActive = true) }
    }

    fun onExitSearchClick() {
        post { it.copy(isSearchActive = false) }
    }

    fun onSearchQueryChange(value: String) {
        post { it.copy(searchQuery = value) }
    }

    fun onClearSearchQueryClick() {
        post { it.copy(searchQuery = "") }
    }

    companion object {
        private const val TAG = "AuthorsListViewModel"
    }
}
