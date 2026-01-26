package com.firdavs.persianliterature.quiz.ui.play

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.model.QuizProgress
import com.firdavs.persianliterature.quiz_api.model.UserAnswer
import com.firdavs.persianliterature.quiz_api.repository.QuestionRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import kotlinx.coroutines.launch
import java.util.UUID

class QuizPlayViewModel(
    private val quizId: String,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val quizProgressRepository: QuizProgressRepository
) : BaseViewModel<QuizPlayUiState>(QuizPlayUiState()) {

    init {
        loadQuiz()
        loadQuestions()
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            quizRepository.getQuiz(quizId).collect { quiz ->
                post { it.copy(quiz = quiz) }
            }
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            questionRepository.getQuestionsByQuizId(quizId).collect { questions ->
                val shuffledQuestions = questions.shuffled().map { question ->
                    question.copy(options = question.options.shuffled())
                }
                post { it.copy(questions = shuffledQuestions) }
            }
        }
    }

    fun onAnswerSelected(answer: String) {
        val currentQuestion = state.value.currentQuestion ?: return
        val updatedAnswers = state.value.userAnswers + (currentQuestion.id to answer)
        post { it.copy(userAnswers = updatedAnswers, showExplanation = true) }
    }

    fun onNextQuestion() {
        val nextIndex = state.value.currentQuestionIndex + 1
        if (nextIndex < state.value.questions.size) {
            post {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    showExplanation = false,
                    questionStartTime = System.currentTimeMillis()
                )
            }
        } else {
            submitQuiz()
        }
    }

    fun onShowQuitDialog() {
        post { it.copy(showQuitDialog = true) }
    }

    fun onDismissQuitDialog() {
        post { it.copy(showQuitDialog = false) }
    }

    private fun submitQuiz() {
        viewModelScope.launch {
            val state = state.value
            val quiz = state.quiz ?: return@launch

            val userAnswersList = state.questions.map { question ->
                val selectedAnswer = state.userAnswers[question.id] ?: ""
                UserAnswer(
                    questionId = question.id,
                    selectedAnswer = selectedAnswer,
                    isCorrect = selectedAnswer == question.correctAnswer,
                    timeSpentSeconds = 10
                )
            }

            val correctAnswers = userAnswersList.count { it.isCorrect }
            val score = quizProgressRepository.calculateScore(correctAnswers, state.questions.size)
            val title = quizProgressRepository.determineTitleEarned(score, quiz.difficulty.name)

            val progressId = UUID.randomUUID().toString()
            val progress = QuizProgress(
                id = progressId,
                quizId = quiz.id,
                score = score,
                correctAnswers = correctAnswers,
                totalQuestions = state.questions.size,
                completedAt = System.currentTimeMillis(),
                timeSpentSeconds = ((System.currentTimeMillis() - state.startTime) / 1000).toInt(),
                answers = userAnswersList,
                titleEarned = title
            )

            quizProgressRepository.saveProgress(progress)
            post { it.copy(isSubmitted = true, progressId = progressId) }
        }
    }
}
