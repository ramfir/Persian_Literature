package com.firdavs.persianliterature.quiz.repository

import android.content.Context
import com.firdavs.persianliterature.quiz.R
import com.firdavs.persianliterature.quiz.db.dao.QuizDao
import com.firdavs.persianliterature.quiz.db.dao.QuizProgressDao
import com.firdavs.persianliterature.quiz.db.model.QuizProgressEntity
import com.firdavs.persianliterature.quiz.db.model.toDomain
import com.firdavs.persianliterature.quiz_api.model.QuizAttemptSummary
import com.firdavs.persianliterature.quiz_api.model.QuizProgress
import com.firdavs.persianliterature.quiz_api.repository.QuizProgressRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class QuizProgressRepositoryImpl(
    private val quizProgressDao: QuizProgressDao,
    private val quizDao: QuizDao,
    private val context: Context
) : QuizProgressRepository {

    override suspend fun saveProgress(progress: QuizProgress) {
        val entity = QuizProgressEntity(
            id = progress.id,
            quizId = progress.quizId,
            score = progress.score,
            correctAnswers = progress.correctAnswers,
            totalQuestions = progress.totalQuestions,
            completedAt = progress.completedAt,
            timeSpentSeconds = progress.timeSpentSeconds,
            answers = Gson().toJson(progress.answers),
            titleEarned = progress.titleEarned
        )
        quizProgressDao.insert(entity)
    }

    override fun getProgressByQuizId(quizId: String): Flow<List<QuizProgress>> {
        return quizProgressDao.getByQuizIdFlow(quizId).map { it.toDomain() }
    }

    override fun getBestAttempt(quizId: String): Flow<QuizProgress?> {
        return quizProgressDao.getBestAttemptFlow(quizId).map { it?.toDomain() }
    }

    override fun getAllAttemptSummaries(): Flow<List<QuizAttemptSummary>> {
        return combine(
            quizDao.getAllFlow(),
            quizProgressDao.getAllProgressFlow()
        ) { quizzes, allProgress ->
            quizzes.map { quiz ->
                val progressForQuiz = allProgress.filter { it.quizId == quiz.id }
                QuizAttemptSummary(
                    quizId = quiz.id,
                    quizTitle = quiz.title,
                    bestScore = progressForQuiz.maxOfOrNull { it.score } ?: 0,
                    attemptCount = progressForQuiz.size,
                    lastAttemptDate = progressForQuiz.maxOfOrNull { it.completedAt },
                    highestTitleEarned = progressForQuiz.maxByOrNull { it.score }?.titleEarned
                )
            }
        }
    }

    @Suppress("MagicNumber")
    override suspend fun calculateScore(correctAnswers: Int, totalQuestions: Int): Int {
        return if (totalQuestions > 0) {
            (correctAnswers * 100) / totalQuestions
        } else {
            0
        }
    }

    @Suppress("MagicNumber")
    override fun determineTitleEarned(score: Int, difficulty: String): String {
        return when (difficulty) {
            "BEGINNER" -> when {
                score >= 90 -> "BEGINNER_MASTER"
                score >= 70 -> "BEGINNER_ADVANCED"
                score >= 60 -> "BEGINNER_INTERMEDIATE"
                else -> "BEGINNER_BASIC"
            }
            "INTERMEDIATE" -> when {
                score >= 90 -> "INTERMEDIATE_MASTER"
                score >= 75 -> "INTERMEDIATE_ADVANCED"
                score >= 60 -> "INTERMEDIATE_INTERMEDIATE"
                else -> "INTERMEDIATE_BASIC"
            }
            "ADVANCED" -> when {
                score >= 95 -> "ADVANCED_MASTER"
                score >= 85 -> "ADVANCED_ADVANCED"
                score >= 70 -> "ADVANCED_INTERMEDIATE"
                else -> "ADVANCED_BASIC"
            }
            "EXPERT" -> when {
                score >= 95 -> "EXPERT_MASTER"
                score >= 90 -> "EXPERT_ADVANCED"
                score >= 80 -> "EXPERT_INTERMEDIATE"
                else -> "EXPERT_BASIC"
            }
            else -> "DEFAULT"
        }
    }

    override fun getTitleStringResource(titleKey: String): Int {
        return when (titleKey) {
            "BEGINNER_MASTER" -> R.string.title_beginner_master
            "BEGINNER_ADVANCED" -> R.string.title_beginner_advanced
            "BEGINNER_INTERMEDIATE" -> R.string.title_beginner_intermediate
            "BEGINNER_BASIC" -> R.string.title_beginner_basic
            "INTERMEDIATE_MASTER" -> R.string.title_intermediate_master
            "INTERMEDIATE_ADVANCED" -> R.string.title_intermediate_advanced
            "INTERMEDIATE_INTERMEDIATE" -> R.string.title_intermediate_intermediate
            "INTERMEDIATE_BASIC" -> R.string.title_intermediate_basic
            "ADVANCED_MASTER" -> R.string.title_advanced_master
            "ADVANCED_ADVANCED" -> R.string.title_advanced_advanced
            "ADVANCED_INTERMEDIATE" -> R.string.title_advanced_intermediate
            "ADVANCED_BASIC" -> R.string.title_advanced_basic
            "EXPERT_MASTER" -> R.string.title_expert_master
            "EXPERT_ADVANCED" -> R.string.title_expert_advanced
            "EXPERT_INTERMEDIATE" -> R.string.title_expert_intermediate
            "EXPERT_BASIC" -> R.string.title_expert_basic
            "DEFAULT" -> R.string.title_default
            else -> R.string.title_default
        }
    }
}
