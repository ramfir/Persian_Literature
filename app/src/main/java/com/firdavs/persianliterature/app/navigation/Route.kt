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
    object AboutApp : Route
}
