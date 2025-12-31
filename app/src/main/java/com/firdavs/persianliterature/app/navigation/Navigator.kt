package com.firdavs.persianliterature.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.firdavs.persianliterature.about_app.ui.AboutAppEntryPoint
import com.firdavs.persianliterature.app.ui.MainActivityUiState
import com.firdavs.persianliterature.author.ui.details.AuthorDetailsEntryPoint
import com.firdavs.persianliterature.author.ui.list.AuthorsListEntryPoint
import com.firdavs.persianliterature.author.ui.work_details.WorkDetailsEntryPoint
import com.firdavs.persianliterature.core.model.Chapter

@Composable
fun Navigator(
    state: MainActivityUiState,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Route.AuthorsList)

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.AuthorsList> {
                AuthorsListEntryPoint(
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) },
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            else -> {}
                        }
                    }
                )
            }
            entry<Route.AuthorDetails> {
                AuthorDetailsEntryPoint(
                    id = it.id,
                    onBackClick = { backStack.back() },
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) }
                )
            }
            entry<Route.WorkDetails> {
                WorkDetailsEntryPoint(
                    id = it.id,
                    onBackClick = { backStack.back() }
                )
            }
            entry<Route.AboutApp> {
                AboutAppEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            else -> {}
                        }
                    }
                )
            }
        }
    )
}
