package com.firdavs.persianliterature.quiz.repository

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
    private val quizDao: QuizDao
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
                score >= 90 -> "Poetry Enthusiast"
                score >= 70 -> "Aspiring Reader"
                score >= 60 -> "Curious Learner"
                else -> "Novice"
            }
            "INTERMEDIATE" -> when {
                score >= 90 -> "Literary Scholar"
                score >= 75 -> "Devoted Reader"
                score >= 60 -> "Poetry Lover"
                else -> "Learning Student"
            }
            "ADVANCED" -> when {
                score >= 95 -> "Master Poet"
                score >= 85 -> "True Connoisseur"
                score >= 70 -> "Skilled Scholar"
                else -> "Determined Student"
            }
            "EXPERT" -> when {
                score >= 95 -> "Literary Genius"
                score >= 90 -> "Grand Master"
                score >= 80 -> "Expert Analyst"
                else -> "Ambitious Scholar"
            }
            else -> "Poetry Novice"
        }
    }
}
