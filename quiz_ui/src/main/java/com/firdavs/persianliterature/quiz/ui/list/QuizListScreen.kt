package com.firdavs.persianliterature.quiz.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.quiz.ui.R
import com.firdavs.persianliterature.quiz_api.model.Quiz
import com.firdavs.persianliterature.quiz_api.model.QuizAttemptSummary
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun QuizListEntryPoint(
    onQuizClick: (String) -> Unit,
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(QuizListViewModel::class) { state, viewModel ->
        QuizListScreen(
            state = state,
            onQuizClick = onQuizClick,
            onChapterClick = onChapterClick,
            onRefreshClick = viewModel::onRefreshClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizListScreen(
    state: QuizListUiState,
    onQuizClick: (String) -> Unit,
    onChapterClick: (Chapter) -> Unit,
    onRefreshClick: () -> Unit
) {
    BaseScreen(
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.Quiz,
                onChapterClick = onChapterClick
            )
        },
        topBar = { drawerState: DrawerState, scope: CoroutineScope ->
            TopBar(
                drawerState = drawerState,
                scope = scope,
                onRefreshClick = onRefreshClick
            )
        },
        mainContent = {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    ProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.quizzes) { quiz ->
                        val summary = state.attemptSummaries.find { it.quizId == quiz.id }
                        QuizCard(
                            quiz = quiz,
                            summary = summary,
                            onClick = { onQuizClick(quiz.id) }
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun TopBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
    onRefreshClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalColors.current.primary)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            scope.launch { drawerState.open() }
        }) {
            Icon(Icons.Default.Menu, "Open drawer")
        }
        Spacer(Modifier.weight(1f))
        H3Text(text = stringResource(R.string.quiz_list_title))
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onRefreshClick) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Refresh"
            )
        }
    }
}

@Composable
private fun QuizCard(
    quiz: Quiz,
    summary: QuizAttemptSummary?,
    onClick: () -> Unit
) {
    val colors = LocalColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            H3Text(text = quiz.title)
            Spacer(modifier = Modifier.height(8.dp))
            T2Text(text = quiz.description)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DifficultyBadge(difficulty = quiz.difficulty.name)
                if (summary != null && summary.bestScore > 0) {
                    T1Text(
                        text = stringResource(R.string.your_best_score, summary.bestScore),
                        color = colors.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            H4Text(
                text = if (summary != null && summary.attemptCount > 0) {
                    stringResource(R.string.retry_quiz)
                } else {
                    stringResource(R.string.start_quiz)
                },
                modifier = Modifier
                    .background(colors.primary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = colors.onPrimary
            )
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: String) {
    val colors = LocalColors.current
    val difficultyText = when (difficulty) {
        "BEGINNER" -> stringResource(R.string.difficulty_beginner)
        "INTERMEDIATE" -> stringResource(R.string.difficulty_intermediate)
        "ADVANCED" -> stringResource(R.string.difficulty_advanced)
        "EXPERT" -> stringResource(R.string.difficulty_expert)
        else -> difficulty
    }

    T1Text(
        text = difficultyText,
        modifier = Modifier
            .background(colors.secondary, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = colors.onSecondary
    )
}
