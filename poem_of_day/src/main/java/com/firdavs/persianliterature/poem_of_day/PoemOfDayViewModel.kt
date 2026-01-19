package com.firdavs.persianliterature.poem_of_day

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class PoemOfDayViewModel(
    private val poemId: String?,
    private val poemRepository: PoemRepository
) : BaseViewModel<PoemOfDayUiState>(PoemOfDayUiState()) {

    init {
        loadPoem()
    }

    private fun loadPoem() {
        viewModelScope.launch {
            val poem = if (poemId != null) {
                poemRepository.getPoemById(poemId)
            } else {
                poemRepository.getRandomPoem()
            }
            post { it.copy(poem = poem, isLoading = false) }
        }
    }

    fun onRefreshClick() {
        post { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            runCatching {
                poemRepository.fetchPoems()
                val poem = poemRepository.getRandomPoem()
                post { it.copy(poem = poem, isRefreshing = false, showToast = true) }
            }.onFailure { error ->
                Log.e(TAG, "onRefreshClick error", error)
                post { it.copy(isRefreshing = false) }
            }
        }
    }

    fun onNewPoemClick() {
        post { it.copy(isLoading = true) }
        viewModelScope.launch {
            val poem = poemRepository.getRandomPoem()
            post { it.copy(poem = poem, isLoading = false) }
        }
    }

    fun resetShowToastFlag() {
        post { it.copy(showToast = false) }
    }

    companion object {
        private const val TAG = "PoemOfDayViewModel"
    }
}
