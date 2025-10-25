package com.firdavs.persianliterature.author_ui.list

import com.firdavs.persianliterature.core.presentation.BaseViewModel

class AuthorsListViewModel :
    BaseViewModel<AuthorsListUiState>(AuthorsListStateProvider().values.toList()[0]) {
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
}
