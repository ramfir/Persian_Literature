package com.firdavs.persianliterature.author.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.dao.WorksDao
import com.firdavs.persianliterature.author.db.model.AuthorEntity
import com.firdavs.persianliterature.author.db.model.WorkEntity

@Database(
    entities = [
        AuthorEntity::class,
        WorkEntity::class
    ],
    version = AuthorsDb.VERSION
)
abstract class AuthorsDb : RoomDatabase() {

    abstract fun getAuthorsDao(): AuthorsDao
    abstract fun getWorksDao(): WorksDao

    companion object {
        const val VERSION = 1
        const val AUTHORS = "authors"
        const val WORKS = "works"
        const val DATABASE_NAME = "authorsDb.db"
    }
}
