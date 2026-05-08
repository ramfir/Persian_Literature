package com.firdavs.persianliterature.author.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.model.PoemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemsDao {
    @Query("DELETE FROM ${AuthorsDb.POEMS}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(poems: List<PoemEntity>)

    @Query("SELECT * FROM ${AuthorsDb.POEMS} ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPoem(): PoemEntity?

    @Query("SELECT * FROM ${AuthorsDb.POEMS} WHERE id = :id")
    suspend fun getPoemById(id: String): PoemEntity?

    @Query("SELECT * FROM ${AuthorsDb.POEMS}")
    fun getAllFlow(): Flow<List<PoemEntity>>

    @Query("SELECT * FROM ${AuthorsDb.POEMS}")
    suspend fun getAllPoems(): List<PoemEntity>

    @Query("SELECT * FROM ${AuthorsDb.POEMS} WHERE isFavourite = 1")
    fun getFavouritesFlow(): Flow<List<PoemEntity>>

    @Query("SELECT id FROM ${AuthorsDb.POEMS} WHERE isFavourite = 1")
    suspend fun getFavouriteIds(): List<String>

    @Query("UPDATE ${AuthorsDb.POEMS} SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavourite(id: String, isFavourite: Boolean)
}
