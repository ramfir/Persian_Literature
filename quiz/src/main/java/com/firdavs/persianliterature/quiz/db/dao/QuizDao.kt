package com.firdavs.persianliterature.quiz.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz.db.model.QuizEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quizzes: List<QuizEntity>)

    @Query("SELECT * FROM ${QuizDb.QUIZZES} ORDER BY `order` ASC")
    fun getAllFlow(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM ${QuizDb.QUIZZES} WHERE id = :id")
    fun getByIdFlow(id: String): Flow<QuizEntity>

    @Query("SELECT * FROM ${QuizDb.QUIZZES} WHERE difficulty = :difficulty ORDER BY `order` ASC")
    fun getByDifficultyFlow(difficulty: String): Flow<List<QuizEntity>>

    @Query("DELETE FROM ${QuizDb.QUIZZES}")
    suspend fun deleteAll()
}
