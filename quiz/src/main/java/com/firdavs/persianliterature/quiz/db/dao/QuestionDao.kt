package com.firdavs.persianliterature.quiz.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.quiz.db.QuizDb
import com.firdavs.persianliterature.quiz.db.model.QuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: List<QuestionEntity>)

    @Query("SELECT * FROM ${QuizDb.QUESTIONS} WHERE quizId = :quizId ORDER BY `order` ASC")
    fun getByQuizIdFlow(quizId: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM ${QuizDb.QUESTIONS} WHERE id = :id")
    fun getByIdFlow(id: String): Flow<QuestionEntity>

    @Query("DELETE FROM ${QuizDb.QUESTIONS}")
    suspend fun deleteAll()
}
