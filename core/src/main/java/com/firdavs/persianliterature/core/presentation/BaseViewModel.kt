package com.firdavs.persianliterature.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<T : UiState>(initialState: T) : ViewModel() {
    protected val stateInner: MutableStateFlow<T> = MutableStateFlow(initialState)
    val state: StateFlow<T> = stateInner.asStateFlow()

    protected fun post(block: (T) -> T) {
        stateInner.update { block.invoke(it) }
    }

    open fun onViewResumed() = Unit
}
