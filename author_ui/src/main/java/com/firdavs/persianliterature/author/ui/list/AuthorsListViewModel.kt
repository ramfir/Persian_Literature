package com.firdavs.persianliterature.author.ui.list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AuthorsListViewModel(
    private val authorRepository: AuthorRepository,
    private val authorUiMapper: AuthorUiMapper,
    private val worksRepository: WorksRepository
) :
    BaseViewModel<AuthorsListUiState>(AuthorsListUiState()) {
    init {
        observeAuthors()
        fetchAuthors()
        fetchWorks()
    }

    private var allAuthors: List<AuthorUiModel> = emptyList()
    private fun observeAuthors() {
        viewModelScope.launch {
            authorRepository.getAuthors().collect { authors ->
                allAuthors = authorUiMapper.map(authors)
                post { it.copy(authors = allAuthors) }
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

    private fun fetchWorks() {
        viewModelScope.launch {
            runCatching {
                worksRepository.fetchWorks()
            }.onFailure {
                Log.e(TAG, "fetchWorks error ", it)
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

    fun filterAuthorsList() {
        val searchQuery = state.value.searchQuery
        val filteredAuthors = allAuthors.filter { author ->
            author.name.contains(searchQuery, ignoreCase = true)
        }
        post { it.copy(authors = filteredAuthors) }
    }

    companion object {
        private const val TAG = "AuthorsListViewModel"
    }
}
