package com.firdavs.persianliterature.app.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository
) : BaseViewModel<MainActivityUiState>(MainActivityUiState()) {

    init {
        fetchAuthors()
        fetchWorks()
    }

    private fun fetchAuthors() {
        viewModelScope.launch {
            runCatching {
                authorRepository.fetchAuthors()
            }.onFailure {
                Log.e(TAG, "fetchAuthors error ", it)
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

    companion object {
        private const val TAG = "MainViewModel"
    }
}
