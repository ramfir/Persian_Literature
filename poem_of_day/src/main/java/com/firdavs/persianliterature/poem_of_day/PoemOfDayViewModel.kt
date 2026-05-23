package com.firdavs.persianliterature.poem_of_day

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.FavouritesRepository
import com.firdavs.persianliterature.author_api.repository.PoemRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class PoemOfDayViewModel(
    private val poemId: String?,
    private val poemRepository: PoemRepository,
    private val favouritesRepository: FavouritesRepository
) : BaseViewModel<PoemOfDayUiState>(PoemOfDayUiState()) {

    init {
        loadPoem()
    }

    private fun loadPoem() {
        viewModelScope.launch {
            val allPoems = poemRepository.getAllPoems()
            if (poemId != null) {
                val poem = poemRepository.getPoemById(poemId)
                val currentIndex = allPoems.indexOfFirst { it.id == poemId }
                post { it.copy(poem = poem, allPoems = allPoems, currentIndex = currentIndex, isLoading = false) }
            } else {
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

    fun onToggleFavourite() {
        val currentPoem = state.value.poem ?: return
        val newIsFavourite = !currentPoem.isFavourite
        viewModelScope.launch {
            favouritesRepository.togglePoemFavourite(currentPoem.id, newIsFavourite)
        }
        val updatedPoem = currentPoem.copy(isFavourite = newIsFavourite)
        post { currentState ->
            val updatedAllPoems = currentState.allPoems.map { poem ->
                if (poem.id == currentPoem.id) updatedPoem else poem
            }
            currentState.copy(poem = updatedPoem, allPoems = updatedAllPoems)
        }
    }
}
