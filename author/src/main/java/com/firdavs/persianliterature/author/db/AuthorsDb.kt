package com.firdavs.persianliterature.author.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.firdavs.persianliterature.author.db.converter.AudioDownloadStatusConverter
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.dao.PoemsDao
import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.firdavs.persianliterature.author.db.model.PoemEntity
import com.firdavs.persianliterature.author.db.model.WorkEntity

@Database(
    entities = [
        AuthorEntity::class,
        WorkEntity::class,
        PoemEntity::class
    ],
    version = AuthorsDb.VERSION
)
@TypeConverters(AudioDownloadStatusConverter::class)
abstract class AuthorsDb : RoomDatabase() {

    abstract fun getAuthorsDao(): AuthorsDao
    abstract fun getWorksDao(): WorksDao
    abstract fun getPoemsDao(): PoemsDao

    companion object {
        const val VERSION = 1
        const val AUTHORS = "authors"
        const val WORKS = "works"
        const val POEMS = "poems"
        const val DATABASE_NAME = "authorsDb.db"
    }
}
