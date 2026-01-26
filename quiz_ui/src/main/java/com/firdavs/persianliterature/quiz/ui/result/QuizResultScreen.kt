package com.firdavs.persianliterature.quiz.ui.result

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.quiz.ui.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun QuizResultEntryPoint(
    progressId: String,
    onRetryClick: (String) -> Unit,
    onBackToListClick: () -> Unit
) {
    BaseEntryPoint(QuizResultViewModel::class, progressId) { state, viewModel ->
        QuizResultScreen(
            state = state,
            onRetryClick = onRetryClick,
            onBackToListClick = onBackToListClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizResultScreen(
    state: QuizResultUiState,
    onRetryClick: (String) -> Unit,
    onBackToListClick: () -> Unit
) {
    val colors = LocalColors.current
    val progress = state.progress
    val quiz = state.quiz

    Scaffold(
        topBar = {
            TopBar(onBackToListClick = onBackToListClick)
        }
    ) { padding ->
        if (progress != null && quiz != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (state.isPassed) {
                            colors.primary.copy(alpha = 0.2f)
                        } else {
                            colors.error.copy(alpha = 0.2f)
                        }
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        H2Text(
                            text = stringResource(R.string.your_score, progress.score),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        H3Text(
                            text = if (state.isPassed) {
                                stringResource(R.string.passed)
                            } else {
                                stringResource(R.string.failed)
                            },
                            color = if (state.isPassed) colors.primary else colors.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        state.titleEarnedStringRes?.let { stringRes ->
                            H4Text(text = stringResource(R.string.title_earned, stringResource(stringRes)))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            T1Text(text = stringResource(R.string.correct_answers))
                            T1Text(
                                text = "${progress.correctAnswers}/${progress.totalQuestions}",
                                color = colors.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            T1Text(text = stringResource(R.string.time_spent))
                            T1Text(
                                text = stringResource(R.string.time_spent_value, progress.timeSpentSeconds),
                                color = colors.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(colors.secondary, RoundedCornerShape(8.dp))
                            .clickable(onClick = onBackToListClick)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        H4Text(text = stringResource(R.string.back_to_list), color = colors.onSecondary)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(colors.primary, RoundedCornerShape(8.dp))
                            .clickable { onRetryClick(quiz.id) }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        H4Text(
                            text = stringResource(R.string.retry_quiz),
                            color = colors.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onBackToListClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalColors.current.primary)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackToListClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }
        Spacer(Modifier.weight(1f))
        H3Text(text = stringResource(R.string.quiz_results))
        Spacer(Modifier.weight(1f))
    }
}
