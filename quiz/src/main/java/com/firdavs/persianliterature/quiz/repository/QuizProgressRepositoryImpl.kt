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
                score >= 90 -> context.getString(R.string.title_beginner_master)
                score >= 70 -> context.getString(R.string.title_beginner_advanced)
                score >= 60 -> context.getString(R.string.title_beginner_intermediate)
                else -> context.getString(R.string.title_beginner_basic)
            }
            "INTERMEDIATE" -> when {
                score >= 90 -> context.getString(R.string.title_intermediate_master)
                score >= 75 -> context.getString(R.string.title_intermediate_advanced)
                score >= 60 -> context.getString(R.string.title_intermediate_intermediate)
                else -> context.getString(R.string.title_intermediate_basic)
            }
            "ADVANCED" -> when {
                score >= 95 -> context.getString(R.string.title_advanced_master)
                score >= 85 -> context.getString(R.string.title_advanced_advanced)
                score >= 70 -> context.getString(R.string.title_advanced_intermediate)
                else -> context.getString(R.string.title_advanced_basic)
            }
            "EXPERT" -> when {
                score >= 95 -> context.getString(R.string.title_expert_master)
                score >= 90 -> context.getString(R.string.title_expert_advanced)
                score >= 80 -> context.getString(R.string.title_expert_intermediate)
                else -> context.getString(R.string.title_expert_basic)
            }
            else -> context.getString(R.string.title_default)
        }
    }
}
