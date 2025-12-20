package com.firdavs.persianliterature.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.firdavs.persianliterature.app.ui.MainActivityUiState
import com.firdavs.persianliterature.author.ui.details.AuthorDetailsEntryPoint
import com.firdavs.persianliterature.author.ui.list.AuthorsListEntryPoint

@Composable
fun Navigator(
    state: MainActivityUiState,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Route.AuthorsList)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route.AuthorsList> {
                AuthorsListEntryPoint(
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) }
                )
            }
            entry<Route.AuthorDetails> { AuthorDetailsEntryPoint(it.id) }
        }
    )
}
