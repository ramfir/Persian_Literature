package com.firdavs.persianliterature.app.navigation

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
import com.firdavs.persianliterature.settings.ui.language.LanguageEntryPoint
import com.firdavs.persianliterature.settings.ui.main.SettingsEntryPoint
import com.firdavs.persianliterature.author.ui.audio_books.AudioBooksEntryPoint
import com.firdavs.persianliterature.author.ui.details.AuthorDetailsEntryPoint
import com.firdavs.persianliterature.author.ui.favourites.FavouritesEntryPoint
import com.firdavs.persianliterature.author.ui.list.AuthorsListEntryPoint
import com.firdavs.persianliterature.author.ui.work_details.WorkDetailsEntryPoint
import com.firdavs.persianliterature.poem_of_day.PoemOfDayEntryPoint
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.quiz.ui.list.QuizListEntryPoint
import com.firdavs.persianliterature.quiz.ui.play.QuizPlayEntryPoint
import com.firdavs.persianliterature.quiz.ui.result.QuizResultEntryPoint
import com.firdavs.persianliterature.ui.kit.theme.localizedContext

@Suppress("MagicNumber", "LongMethod")
@Composable
fun Navigator(
    state: MainActivityUiState,
    modifier: Modifier = Modifier,
    onNavigationHandled: () -> Unit = {}
) {
    val backStack = rememberNavBackStack(Route.AuthorsList)
    val context = LocalContext.current
    val toastContext = localizedContext()
    val coroutineScope = rememberCoroutineScope()
    var lastBackPressed by remember { mutableLongStateOf(0L) }

    // Handle notification navigation
    LaunchedEffect(state.notificationPoemId) {
        state.notificationPoemId?.let { poemId ->
            backStack.startNewRoot(Route.PoemOfDay(poemId))
            onNavigationHandled()
        }
    }

    val onBack: () -> Unit = {
        val isRootScreen = backStack.size == 1

        if (isRootScreen) {
            if (System.currentTimeMillis() - lastBackPressed < 3000) {
                (context as? Activity)?.finish()
            } else {
                lastBackPressed = System.currentTimeMillis()
                Toast.makeText(
                    toastContext,
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
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    }
                )
            }
            entry<Route.PoemOfDay> {
                PoemOfDayEntryPoint(
                    poemId = it.poemId,
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
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
            entry<Route.AudioBooks> {
                AudioBooksEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    },
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) }
                )
            }
            entry<Route.Quiz> {
                QuizListEntryPoint(
                    onQuizClick = { backStack.next(Route.QuizPlay(it)) },
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            Chapter.Settings -> backStack.startNewRoot(Route.Settings)
                            else -> {}
                        }
                    }
                )
            }
            entry<Route.QuizPlay> {
                QuizPlayEntryPoint(
                    quizId = it.id,
                    onBackClick = onBack,
                    onQuizComplete = { progressId ->
                        backStack.next(Route.QuizResult(progressId))
                    }
                )
            }
            entry<Route.QuizResult> {
                QuizResultEntryPoint(
                    progressId = it.progressId,
                    onRetryClick = { quizId ->
                        coroutineScope.launch {
                            backStack.back()
                            delay(50)
                            backStack.back()
                            delay(50)
                            backStack.next(Route.QuizPlay(quizId))
                        }
                    },
                    onBackToListClick = {
                        backStack.startNewRoot(Route.Quiz)
                    }
                )
            }
            entry<Route.AboutApp> {
                AboutAppEntryPoint(
                    onChapterClick = { chapter ->
                        when (chapter) {
                            Chapter.Authors -> backStack.startNewRoot(Route.AuthorsList)
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
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
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
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
                            Chapter.PoemOfDay -> backStack.startNewRoot(Route.PoemOfDay())
                            Chapter.AudioBooks -> backStack.startNewRoot(Route.AudioBooks)
                            Chapter.Quiz -> backStack.startNewRoot(Route.Quiz)
                            Chapter.AboutApp -> backStack.startNewRoot(Route.AboutApp)
                            Chapter.Favourites -> backStack.startNewRoot(Route.Favourites)
                            else -> {}
                        }
                    },
                    onChangeLanguageClick = { backStack.next(Route.Language) }
                )
            }
            entry<Route.Language> {
                LanguageEntryPoint(
                    onBackClick = onBack
                )
            }
        }
    )
}
