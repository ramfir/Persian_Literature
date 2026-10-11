package com.firdavs.persianliterature.suggest_poem

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.model.PoemSuggestion
import com.firdavs.persianliterature.author_api.repository.PoemSuggestionRepository
import com.firdavs.persianliterature.author_api.repository.SuggestionRejectedException
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import kotlinx.coroutines.launch

class SuggestPoemViewModel(
    private val poemSuggestionRepository: PoemSuggestionRepository
) : BaseViewModel<SuggestPoemUiState>(SuggestPoemUiState()) {

    fun onTitleChange(value: String) = post {
        it.copy(title = value.take(TITLE_MAX_LENGTH), errorRes = null)
    }

    fun onAuthorChange(value: String) = post {
        it.copy(author = value.take(AUTHOR_MAX_LENGTH), errorRes = null)
    }

    fun onTextChange(value: String) = post {
        it.copy(text = value.take(TEXT_MAX_LENGTH), errorRes = null)
    }

    fun onSuggesterNameChange(value: String) = post {
        it.copy(suggesterName = value.take(NAME_MAX_LENGTH), errorRes = null)
    }

    fun onSubmitClick() {
        val currentState = state.value
        if (currentState.isSubmitting) return
        val errorRes = validate(currentState)
        if (errorRes != null) {
            post { it.copy(errorRes = errorRes) }
            return
        }
        submit(currentState)
    }

    fun onSendAnotherClick() {
        post { SuggestPoemUiState() }
    }

    // Длины должны совпадать с проверками в правилах Firestore для poem_suggestions:
    // если клиент мягче правил, запись отклоняется с PERMISSION_DENIED и пользователь
    // видит сообщение про недоступность отправки, хотя в banned_users его нет.
    private fun validate(currentState: SuggestPoemUiState): Int? = when {
        poemSuggestionRepository.getRemainingSuggestionsToday() <= 0 ->
            R.string.suggest_poem_error_limit
        currentState.author.isBlank() || currentState.text.isBlank() ->
            R.string.suggest_poem_error_empty_fields
        currentState.text.trim().length < TEXT_MIN_LENGTH -> R.string.suggest_poem_error_short_text
        else -> null
    }

    @Suppress("SwallowedException")
    private fun submit(currentState: SuggestPoemUiState) {
        post { it.copy(isSubmitting = true, errorRes = null) }
        viewModelScope.launch {
            val suggestion = PoemSuggestion(
                title = currentState.title,
                text = currentState.text,
                author = currentState.author,
                suggesterName = currentState.suggesterName
            )
            try {
                poemSuggestionRepository.submitSuggestion(suggestion)
                post { it.copy(isSubmitting = false, isSubmitted = true) }
            } catch (e: SuggestionRejectedException) {
                post {
                    it.copy(
                        isSubmitting = false,
                        errorRes = R.string.suggest_poem_error_rejected
                    )
                }
            } catch (e: Exception) {
                post {
                    it.copy(
                        isSubmitting = false,
                        errorRes = R.string.suggest_poem_error_network
                    )
                }
            }
        }
    }

    private companion object {
        const val TITLE_MAX_LENGTH = 100
        const val AUTHOR_MAX_LENGTH = 100
        const val TEXT_MAX_LENGTH = 3000
        const val NAME_MAX_LENGTH = 50
        const val TEXT_MIN_LENGTH = 20
    }
}
