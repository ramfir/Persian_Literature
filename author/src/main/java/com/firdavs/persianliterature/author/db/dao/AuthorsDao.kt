package com.firdavs.persianliterature.author.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.model.AuthorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorsDao {

    @Query("DELETE FROM ${AuthorsDb.AUTHORS}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authors: List<AuthorEntity>)

    @Query("SELECT * FROM ${AuthorsDb.AUTHORS}")
    fun getAllFlow(): Flow<List<AuthorEntity>>
}
