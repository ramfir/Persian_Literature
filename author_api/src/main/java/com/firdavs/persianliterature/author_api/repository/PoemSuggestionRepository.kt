package com.firdavs.persianliterature.author_api.repository

import com.firdavs.persianliterature.author_api.model.PoemSuggestion

interface PoemSuggestionRepository {

    /**
     * Uploads the suggestion to the moderation queue and counts it against today's quota.
     * Throws if the upload fails, so nothing is counted for a failed attempt.
     */
    suspend fun submitSuggestion(suggestion: PoemSuggestion)

    /**
     * How many suggestions the user may still send today. Not shown in the UI:
     * the ceiling only guards against flooding and surfaces when it is hit.
     */
    fun getRemainingSuggestionsToday(): Int

    companion object {
        const val DAILY_LIMIT = 20
    }
}
