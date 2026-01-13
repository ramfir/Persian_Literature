package com.firdavs.persianliterature.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface Route : NavKey {
    @Serializable
    object AuthorsList : Route

    @Serializable
    data class AuthorDetails(val id: String) : Route

    @Serializable
    data class WorkDetails(val id: String) : Route

    @Serializable
    object AudioBooks : Route

    @Serializable
    object Quiz : Route

    @Serializable
    data class QuizPlay(val id: String, val sessionId: Long = System.currentTimeMillis()) : Route

    @Serializable
    data class QuizResult(val progressId: String) : Route

    @Serializable
    object AboutApp : Route

    @Serializable
    object Favourites : Route

    @Serializable
    object Settings : Route

    @Serializable
    object Language : Route
}
