package com.firdavs.persianliterature.author.ui.all_works

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class AllWorksViewModel(
    private val authorRepository: AuthorRepository
) : BaseViewModel<AllWorksUiState>(AllWorksUiState()) {

    init {
        observeAuthorsWithWorks()
    }

    private var allGroups: List<AuthorWorksGroup> = emptyList()

    private fun observeAuthorsWithWorks() {
        viewModelScope.launch {
            authorRepository.getAuthorsWithAtLeastTwoWorks().collect { authorsWithWorks ->
                allGroups = authorsWithWorks.toGroups()
                applySearchFilter()
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

    fun applySearchFilter() {
        val searchQuery = state.value.searchQuery

        if (searchQuery.isBlank()) {
            post { it.copy(groups = allGroups, isLoading = false) }
            return
        }

        val filteredGroups = allGroups.mapNotNull { group ->
            val authorMatches = group.authorName.contains(searchQuery, ignoreCase = true)
            val filteredWorks = if (authorMatches) {
                group.works
            } else {
                group.works.filter { it.title.contains(searchQuery, ignoreCase = true) }
            }
            if (authorMatches || filteredWorks.isNotEmpty()) {
                group.copy(works = filteredWorks)
            } else {
                null
            }
        }

        post { it.copy(groups = filteredGroups, isLoading = false) }
    }
}
