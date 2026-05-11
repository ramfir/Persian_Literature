package com.firdavs.persianliterature.quiz.ui.play

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.quiz.ui.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.buttons.PrimaryButton
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun QuizPlayEntryPoint(
    quizId: String,
    onBackClick: () -> Unit,
    onQuizComplete: (String) -> Unit
) {
    BaseEntryPoint(QuizPlayViewModel::class, quizId) { state, viewModel ->
        LaunchedEffect(state.isSubmitted) {
            if (state.isSubmitted && state.progressId != null) {
                onQuizComplete(state.progressId)
            }
        }

        QuizPlayScreen(
            state = state,
            onBackClick = onBackClick,
            onAnswerSelected = viewModel::onAnswerSelected,
            onConfirmAnswer = viewModel::onConfirmAnswer,
            onNextQuestion = viewModel::onNextQuestion,
            onShowQuitDialog = viewModel::onShowQuitDialog,
            onDismissQuitDialog = viewModel::onDismissQuitDialog
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizPlayScreen(
    state: QuizPlayUiState,
    onBackClick: () -> Unit,
    onAnswerSelected: (String) -> Unit,
    onConfirmAnswer: () -> Unit,
    onNextQuestion: () -> Unit,
    onShowQuitDialog: () -> Unit,
    onDismissQuitDialog: () -> Unit
) {
    val colors = LocalColors.current
    val currentQuestion = state.currentQuestion

    BackHandler {
        onShowQuitDialog()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = state.quiz?.title ?: "",
                onLeaveClick = onShowQuitDialog
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { state.progress },
                modifier = Modifier.fillMaxWidth(),
                color = colors.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            T1Text(
                text = stringResource(
                    R.string.question_progress,
                    state.currentQuestionIndex + 1,
                    state.questions.size
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (currentQuestion != null) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    H3Text(text = currentQuestion.questionText)
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        currentQuestion.options.forEach { option ->
                            val confirmedAnswer = state.userAnswers[currentQuestion.id]
                            val isConfirmed = confirmedAnswer == option
                            val isSelectedButNotConfirmed =
                                state.selectedButUnconfirmedAnswer == option
                            val isSelected = isConfirmed || isSelectedButNotConfirmed
                            val isCorrect = option == currentQuestion.correctAnswer
                            val showCorrect = state.showExplanation && isCorrect
                            val showIncorrect = state.showExplanation && isConfirmed && !isCorrect
                            OptionCard(
                                text = option,
                                isSelected = isSelected,
                                showCorrect = showCorrect,
                                showIncorrect = showIncorrect,
                                enabled = !state.showExplanation,
                                onClick = { onAnswerSelected(option) }
                            )
                        }
                    }

                    if (!state.showExplanation && state.selectedButUnconfirmedAnswer != null) {
                        Spacer(modifier = Modifier.weight(1f))
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.confirm_answer),
                            onClick = onConfirmAnswer
                        )
                    }

                    if (state.showExplanation) {
                        Spacer(modifier = Modifier.height(24.dp))
                        val isCorrect =
                            state.userAnswers[currentQuestion.id] == currentQuestion.correctAnswer
                        H4Text(
                            text = stringResource(
                                if (isCorrect) R.string.correct_answer else R.string.wrong_answer
                            ),
                            color = if (isCorrect) colors.tertiary else colors.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = colors.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                H4Text(text = stringResource(R.string.explanation))
                                Spacer(modifier = Modifier.height(8.dp))
                                T2Text(text = currentQuestion.explanation)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = if (state.isLastQuestion) {
                                stringResource(R.string.finish_quiz)
                            } else {
                                stringResource(R.string.next_question)
                            },
                            onClick = onNextQuestion
                        )
                    }
                }
            }
        }

        if (state.showQuitDialog) {
            QuitQuizDialog(
                onDismiss = onDismissQuitDialog,
                onConfirm = {
                    onDismissQuitDialog()
                    onBackClick()
                }
            )
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    onLeaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalColors.current.primary)
            .statusBarsPadding()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        H3Text(
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onLeaveClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Leave quiz"
            )
        }
    }
}

@Composable
private fun QuitQuizDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                H4Text(
                    text = stringResource(R.string.quit_quiz_title),
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                PrimaryButton(
                    text = stringResource(R.string.yes),
                    onClick = onConfirm
                )
                PrimaryButton(
                    text = stringResource(R.string.no),
                    onClick = onDismiss
                )
            }
        },
        confirmButton = {},
        dismissButton = {}
    )
}

@Composable
private fun OptionCard(
    text: String,
    isSelected: Boolean,
    showCorrect: Boolean,
    showIncorrect: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val colors = LocalColors.current
    val backgroundColor = when {
        showCorrect -> colors.tertiary.copy(alpha = 0.3f)
        showIncorrect -> colors.error.copy(alpha = 0.3f)
        isSelected -> colors.primary
        else -> colors.primary.copy(alpha = 0.3f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            )
            .then(
                if (isSelected && !showCorrect && !showIncorrect) {
                    Modifier.border(2.dp, colors.primary, RoundedCornerShape(8.dp))
                } else Modifier
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        T1Text(
            text = text,
            modifier = Modifier.padding(16.dp)
        )
    }
}
