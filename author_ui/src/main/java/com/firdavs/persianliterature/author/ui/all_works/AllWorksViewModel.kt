package com.firdavs.persianliterature.author.ui.all_works

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author.ui.mapper.AuthorUiMapper
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AllWorksViewModel(
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val authorUiMapper: AuthorUiMapper
) : BaseViewModel<AllWorksUiState>(AllWorksUiState()) {

    init {
        observeAuthorsWithWorks()
    }

    private var allItems: List<AllWorksListItem> = emptyList()

    private fun observeAuthorsWithWorks() {
        viewModelScope.launch {
            authorRepository.getAuthors().collect { authors ->
                val authorUiModels = authorUiMapper.map(authors)

                if (authorUiModels.isEmpty()) {
                    post { it.copy(items = emptyList(), isLoading = false) }
                    return@collect
                }

                // Collect works for all authors and combine them
                val worksFlows = authorUiModels.map { author ->
                    worksRepository.getWorksByAuthorId(author.id)
                }

                combine(worksFlows) { worksArrays ->
                    // Create map of author to their works
                    authorUiModels.zip(worksArrays.toList()).toMap()
                }.collect { authorsToWorksMap ->
                    allItems = authorsToWorksMap.toListItems()
                    applySearchFilter()
                }
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
                    val authorMatches = item.author.name.contains(searchQuery, ignoreCase = true)
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
