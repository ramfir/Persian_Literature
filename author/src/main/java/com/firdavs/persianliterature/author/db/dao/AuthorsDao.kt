package com.firdavs.persianliterature.author.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.firdavs.persianliterature.author.db.AuthorsDb
import com.firdavs.persianliterature.author.db.model.AuthorEntity
import kotlinx.coroutines.flow.Flow

data class AuthorWithWorksRow(
    val id: String,
    val name: String,
    val work_id: String?,
    val work_authorId: String?,
    val work_title: String?,
    val work_description: String?,
    val work_publishYear: String?,
    val work_fileUrl: String?,
    val work_audioUrl: String?,
    val work_audioCacheStatus: String?,
    val work_audioContentLength: Long?,
    val work_audioCachedBytes: Long?,
    val work_audioDownloadStatus: String?,
    val work_audioLocalPath: String?,
    val work_isFavourite: Boolean?
)

@Dao
interface AuthorsDao {

    @Query("DELETE FROM ${AuthorsDb.AUTHORS}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(authors: List<AuthorEntity>)

    @Query("SELECT * FROM ${AuthorsDb.AUTHORS} ORDER BY name COLLATE NOCASE")
    fun getAllFlow(): Flow<List<AuthorEntity>>

    @Query("SELECT * FROM ${AuthorsDb.AUTHORS} WHERE id = :id")
    fun getByIdFlow(id: String): Flow<AuthorEntity>

    @Query("SELECT * FROM ${AuthorsDb.AUTHORS} WHERE isFavourite = 1 ORDER BY name COLLATE NOCASE")
    fun getFavouritesFlow(): Flow<List<AuthorEntity>>

    @Query("UPDATE ${AuthorsDb.AUTHORS} SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateFavourite(id: String, isFavourite: Boolean)

    @Query("SELECT id FROM ${AuthorsDb.AUTHORS} WHERE isFavourite = 1")
    suspend fun getFavouriteIds(): List<String>

    @Query("SELECT * FROM ${AuthorsDb.AUTHORS} ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomAuthor(): AuthorEntity?

    @Query(
        """
        SELECT
            a.id,
            a.name,
            w.id as work_id,
            w.authorId as work_authorId,
            w.title as work_title,
            w.description as work_description,
            w.publishYear as work_publishYear,
            w.fileUrl as work_fileUrl,
            w.audioUrl as work_audioUrl,
            w.audioCacheStatus as work_audioCacheStatus,
            w.audioContentLength as work_audioContentLength,
            w.audioCachedBytes as work_audioCachedBytes,
            w.audioDownloadStatus as work_audioDownloadStatus,
            w.audioLocalPath as work_audioLocalPath,
            w.isFavourite as work_isFavourite
        FROM ${AuthorsDb.AUTHORS} a
        LEFT JOIN ${AuthorsDb.WORKS} w ON a.id = w.authorId
        ORDER BY a.name COLLATE NOCASE, w.title COLLATE NOCASE
        """
    )
    fun getAllAuthorsWithWorksFlow(): Flow<List<AuthorWithWorksRow>>

    @Query(
        """
        SELECT
            a.id,
            a.name,
            w.id as work_id,
            w.authorId as work_authorId,
            w.title as work_title,
            w.description as work_description,
            w.publishYear as work_publishYear,
            w.fileUrl as work_fileUrl,
            w.audioUrl as work_audioUrl,
            w.audioCacheStatus as work_audioCacheStatus,
            w.audioContentLength as work_audioContentLength,
            w.audioCachedBytes as work_audioCachedBytes,
            w.audioDownloadStatus as work_audioDownloadStatus,
            w.audioLocalPath as work_audioLocalPath,
            w.isFavourite as work_isFavourite
        FROM ${AuthorsDb.AUTHORS} a
        INNER JOIN ${AuthorsDb.WORKS} w ON a.id = w.authorId
        WHERE a.id IN (
            SELECT authorId FROM ${AuthorsDb.WORKS} GROUP BY authorId HAVING COUNT(*) >= 2
        )
        ORDER BY a.name COLLATE NOCASE, w.title COLLATE NOCASE
        """
    )
    fun getAuthorsWithAtLeastTwoWorksFlow(): Flow<List<AuthorWithWorksRow>>
}
