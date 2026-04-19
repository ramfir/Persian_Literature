package com.firdavs.persianliterature.quiz.ui.result

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import com.firdavs.persianliterature.quiz_api.repository.QuizRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val progressId: String,
    private val quizRepository: QuizRepository,
    private val quizProgressRepository: QuizProgressRepository
) : BaseViewModel<QuizResultUiState>(QuizResultUiState()) {

    init {
        loadResult()
    }

    private fun loadResult() {
        viewModelScope.launch {
            quizProgressRepository.getAllAttemptSummaries().first().let { summaries ->
                summaries.forEach { summary ->
                    quizProgressRepository.getProgressByQuizId(summary.quizId).first().forEach { progress ->
                        if (progress.id == progressId) {
                            val quiz = quizRepository.getQuiz(progress.quizId).first()
                            val isPassed = progress.score >= quiz.passingScore
                            val titleStringRes = quizProgressRepository.getTitleStringResource(progress.titleEarned)
                            post {
                                it.copy(
                                    quiz = quiz,
                                    progress = progress,
                                    isPassed = isPassed,
                                    titleEarnedStringRes = titleStringRes
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
