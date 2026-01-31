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
            if (poemId != null) {
                val poem = poemRepository.getPoemById(poemId)
                post { it.copy(poem = poem, isLoading = false) }
            } else {
                val allPoems = poemRepository.getAllPoems()
                if (allPoems.isNotEmpty()) {
                    val randomIndex = allPoems.indices.random()
                    val poem = allPoems[randomIndex]
                    post { it.copy(poem = poem, allPoems = allPoems, currentIndex = randomIndex, isLoading = false) }
                } else {
                    post { it.copy(isLoading = false) }
                }
            }
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
        post { currentState ->
            val allPoems = currentState.allPoems
            if (allPoems.isEmpty()) {
                currentState
            } else {
                val nextIndex = (currentState.currentIndex + 1) % allPoems.size
                val nextPoem = allPoems[nextIndex]
                currentState.copy(poem = nextPoem, currentIndex = nextIndex)
            }
        }
    }

    fun onPreviousPoemClick() {
        post { currentState ->
            val allPoems = currentState.allPoems
            if (allPoems.isEmpty()) {
                currentState
            } else {
                val previousIndex = if (currentState.currentIndex - 1 < 0) {
                    allPoems.size - 1
                } else {
                    currentState.currentIndex - 1
                }
                val previousPoem = allPoems[previousIndex]
                currentState.copy(poem = previousPoem, currentIndex = previousIndex)
            }
        }
    }

    fun resetShowToastFlag() {
        post { it.copy(showToast = false) }
    }

    companion object {
        private const val TAG = "PoemOfDayViewModel"
    }
}
