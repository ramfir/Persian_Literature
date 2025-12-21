package com.firdavs.persianliterature.author.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.model.WorkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(works: List<WorkEntity>)

    @Query("SELECT * FROM ${AuthorsDb.WORKS} WHERE authorId = :id")
    fun getByAuthorIdFlow(id: String): Flow<List<WorkEntity>>

    @Query("SELECT * FROM ${AuthorsDb.WORKS} WHERE id = :id")
    fun getByIdFlow(id: String): Flow<WorkEntity>
}
