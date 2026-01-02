package com.firdavs.persianliterature.app.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.firdavs.persianliterature.about_app.ui.AboutAppEntryPoint
import com.firdavs.persianliterature.app.R
import com.firdavs.persianliterature.app.ui.MainActivityUiState
import com.firdavs.persianliterature.settings.SettingsEntryPoint
import com.firdavs.persianliterature.author.ui.details.AuthorDetailsEntryPoint
import com.firdavs.persianliterature.author.ui.favourites.FavouritesEntryPoint
import com.firdavs.persianliterature.author.ui.list.AuthorsListEntryPoint
import com.firdavs.persianliterature.author.ui.work_details.WorkDetailsEntryPoint
import com.firdavs.persianliterature.core.model.Chapter

@Composable
fun Navigator(
    state: MainActivityUiState,
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Route.AuthorsList)
    val context = LocalContext.current
    var lastBackPressed by remember { mutableLongStateOf(0L) }

    val onBack: () -> Unit = {
        val isRootScreen = backStack.size == 1

        if (isRootScreen) {
            if (System.currentTimeMillis() - lastBackPressed < 3000) {
                (context as? Activity)?.finish()
            } else {
                lastBackPressed = System.currentTimeMillis()
                Toast.makeText(
                    context,
                    R.string.exit_toast_message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            backStack.back()
        }
    }

    BackHandler(onBack = onBack)

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
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    }
                )
            }
            entry<Route.AuthorDetails> {
                AuthorDetailsEntryPoint(
                    id = it.id,
                    onBackClick = onBack,
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) }
                )
            }
            entry<Route.WorkDetails> {
                WorkDetailsEntryPoint(
                    id = it.id,
                    onBackClick = onBack
                )
            }
            entry<Route.AboutApp> {
                AboutAppEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    }
                )
            }
            entry<Route.Favourites> {
                FavouritesEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    },
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) },
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) }
                )
            }
            entry<Route.Settings> {
                SettingsEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            else -> {}
                        }
                    }
                )
            }
        }
    )
}
