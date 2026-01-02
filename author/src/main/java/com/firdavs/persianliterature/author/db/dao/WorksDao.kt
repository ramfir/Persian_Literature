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

    @Query("SELECT * FROM ${AuthorsDb.WORKS} WHERE isFavourite = 1")
    fun getFavouritesFlow(): Flow<List<WorkEntity>>

    @Query("UPDATE ${AuthorsDb.WORKS} SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavourite(id: String, isFavourite: Boolean)

    @Query("SELECT id FROM ${AuthorsDb.WORKS} WHERE isFavourite = 1")
    suspend fun getFavouriteIds(): List<String>
}
