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

    private var allItems: List<AllWorksListItem> = emptyList()

    private fun observeAuthorsWithWorks() {
        viewModelScope.launch {
            authorRepository.getAllAuthorsWithWorks().collect { authorsWithWorks ->
                allItems = authorsWithWorks.toListItems()
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

    @Suppress("NestedBlockDepth")
    fun applySearchFilter() {
        val searchQuery = state.value.searchQuery

        if (searchQuery.isBlank()) {
            post { it.copy(items = allItems, isLoading = false) }
            return
        }

        val filteredItems = mutableListOf<AllWorksListItem>()
        var currentAuthorHeader: AllWorksListItem.AuthorHeader? = null
        val currentAuthorWorks = mutableListOf<AllWorksListItem>()

        for (item in allItems) {
            when (item) {
                is AllWorksListItem.AuthorHeader -> {
                    // Save previous author's works if any matched
                    if (currentAuthorHeader != null && currentAuthorWorks.isNotEmpty()) {
                        filteredItems.add(currentAuthorHeader)
                        filteredItems.addAll(currentAuthorWorks)
                    }

                    // Start tracking new author
                    currentAuthorHeader = item
                    currentAuthorWorks.clear()

                    // Check if author name matches
                    val authorMatches = item.authorName.contains(searchQuery, ignoreCase = true)
                    if (authorMatches) {
                        // If author matches, we'll include all their works
                        currentAuthorWorks.add(item) // Placeholder, will be replaced
                    }
                }
                is AllWorksListItem.WorkItem -> {
                    val workMatches = item.work.title.contains(searchQuery, ignoreCase = true)
                    val authorMatches = currentAuthorWorks.isNotEmpty()

                    if (workMatches || authorMatches) {
                        // Clear placeholder if this is first work match
                        if (currentAuthorWorks.size == 1 && currentAuthorWorks[0] is AllWorksListItem.AuthorHeader) {
                            currentAuthorWorks.clear()
                        }
                        currentAuthorWorks.add(item)
                    }
                }
                is AllWorksListItem.EmptyWorksMessage -> {
                    val authorMatches = currentAuthorWorks.isNotEmpty()
                    if (authorMatches) {
                        currentAuthorWorks.clear()
                        currentAuthorWorks.add(item)
                    }
                }
            }
        }

        // Add last author's works if any matched
        if (currentAuthorHeader != null && currentAuthorWorks.isNotEmpty()) {
            filteredItems.add(currentAuthorHeader)
            filteredItems.addAll(currentAuthorWorks)
        }

        post { it.copy(items = filteredItems, isLoading = false) }
    }
}
