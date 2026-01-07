package com.firdavs.persianliterature.author.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.model.WorkEntity
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import kotlinx.coroutines.flow.Flow

data class WorkAudioInfo(
    val id: String,
    val audioDownloadStatus: AudioDownloadStatus,
    val audioLocalPath: String?
)

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
    @Query(
        """
        SELECT id, audioDownloadStatus, audioLocalPath 
        FROM ${AuthorsDb.WORKS} 
        WHERE audioDownloadStatus = 'DOWNLOADED'
        """
    )
    suspend fun getDownloadedAudioInfo(): List<WorkAudioInfo>

    @Query(
        """
        UPDATE ${AuthorsDb.WORKS} 
        SET audioDownloadStatus = :status, audioLocalPath = :localPath 
        WHERE id = :id
        """
    )
    suspend fun updateAudioDownloadStatus(id: String, status: AudioDownloadStatus, localPath: String?)

    @Query("UPDATE ${AuthorsDb.WORKS} SET audioDownloadStatus = :status WHERE id = :id")
    suspend fun updateAudioDownloadStatusOnly(id: String, status: AudioDownloadStatus)

    @Query(
        """
        SELECT * 
        FROM ${AuthorsDb.WORKS} 
        WHERE audioUrl IS NOT NULL AND audioDownloadStatus = 'DOWNLOADED'
        """
    )
    fun getWorksWithDownloadedAudio(): Flow<List<WorkEntity>>
}
