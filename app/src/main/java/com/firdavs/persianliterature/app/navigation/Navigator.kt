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
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.firdavs.persianliterature.about_app.ui.AboutAppEntryPoint
import com.firdavs.persianliterature.app.R
import com.firdavs.persianliterature.app.ui.MainActivityUiState
import com.firdavs.persianliterature.settings.ui.language.LanguageEntryPoint
import com.firdavs.persianliterature.settings.ui.main.SettingsEntryPoint
import com.firdavs.persianliterature.author.ui.all_works.AllWorksEntryPoint
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
import com.firdavs.persianliterature.suggest_poem.SuggestPoemEntryPoint
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
    var lastNavigatedBackTime by remember { mutableLongStateOf(0L) }

    // Handle notification navigation
    LaunchedEffect(state.notificationPoemId) {
        state.notificationPoemId?.let { poemId ->
            backStack.startNewRoot(Route.PoemOfDay(poemId))
            onNavigationHandled()
        }
    }

    // Handle new works dialog navigation
    LaunchedEffect(state.navigationWorkId) {
        state.navigationWorkId?.let { workId ->
            backStack.next(Route.WorkDetails(workId))
            // Clear the navigation work ID after handling
            coroutineScope.launch {
                delay(100) // Small delay to ensure navigation completes
                onNavigationHandled()
            }
        }
    }

    val onBack: () -> Unit = {
        val isRootScreen = backStack.size == 1

        if (isRootScreen) {
            val recentlyNavigatedBack = System.currentTimeMillis() - lastNavigatedBackTime < 700L
            if (!recentlyNavigatedBack) {
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
            }
        } else {
            lastNavigatedBackTime = System.currentTimeMillis()
            backStack.back()
        }
    }

    BackHandler(onBack = onBack)

    val onChapterClick: (Chapter) -> Unit = { chapter ->
        backStack.startNewRoot(chapter.toRoute())
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Route.AuthorsList> {
                AuthorsListEntryPoint(
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) },
                    onChapterClick = onChapterClick
                )
            }
            entry<Route.AllWorks> {
                AllWorksEntryPoint(
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) },
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) },
                    onChapterClick = onChapterClick
                )
            }
            entry<Route.PoemOfDay> {
                PoemOfDayEntryPoint(
                    poemId = it.poemId,
                    onSuggestPoemClick = { backStack.next(Route.SuggestPoem) },
                    onChapterClick = onChapterClick
                )
            }
            entry<Route.SuggestPoem> {
                SuggestPoemEntryPoint(
                    onChapterClick = onChapterClick
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
                    onChapterClick = onChapterClick,
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) }
                )
            }
            entry<Route.Quiz> {
                QuizListEntryPoint(
                    onQuizClick = { backStack.next(Route.QuizPlay(it)) },
                    onChapterClick = onChapterClick
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
                    onChapterClick = onChapterClick
                )
            }
            entry<Route.Favourites> {
                FavouritesEntryPoint(
                    onChapterClick = onChapterClick,
                    onAuthorClick = { backStack.next(Route.AuthorDetails(it)) },
                    onWorkClick = { backStack.next(Route.WorkDetails(it)) },
                    onPoemClick = { backStack.next(Route.PoemOfDay(it)) }
                )
            }
            entry<Route.Settings> {
                SettingsEntryPoint(
                    onChapterClick = onChapterClick,
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

private fun Chapter.toRoute(): Route = when (this) {
    Chapter.Authors -> Route.AuthorsList
    Chapter.AllWorks -> Route.AllWorks
    Chapter.PoemOfDay -> Route.PoemOfDay()
    Chapter.SuggestPoem -> Route.SuggestPoem
    Chapter.AudioBooks -> Route.AudioBooks
    Chapter.Quiz -> Route.Quiz
    Chapter.Favourites -> Route.Favourites
    Chapter.Settings -> Route.Settings
    Chapter.AboutApp -> Route.AboutApp
}
