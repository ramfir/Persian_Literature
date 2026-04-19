package com.firdavs.persianliterature.quiz.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz.db.model.QuizProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: QuizProgressEntity)

    @Query("SELECT * FROM ${QuizDb.QUIZ_PROGRESS} WHERE quizId = :quizId ORDER BY completedAt DESC")
    fun getByQuizIdFlow(quizId: String): Flow<List<QuizProgressEntity>>

    @Query("SELECT * FROM ${QuizDb.QUIZ_PROGRESS} WHERE quizId = :quizId ORDER BY score DESC LIMIT 1")
    fun getBestAttemptFlow(quizId: String): Flow<QuizProgressEntity?>

    @Query("SELECT * FROM ${QuizDb.QUIZ_PROGRESS} ORDER BY completedAt DESC")
    fun getAllProgressFlow(): Flow<List<QuizProgressEntity>>

    @Query("SELECT COUNT(*) FROM ${QuizDb.QUIZ_PROGRESS} WHERE quizId = :quizId")
    suspend fun getAttemptCount(quizId: String): Int

    @Query("DELETE FROM ${QuizDb.QUIZ_PROGRESS}")
    suspend fun deleteAll()
}
