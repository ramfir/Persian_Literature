package com.firdavs.persianliterature.author.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.firdavs.persianliterature.author.db.dao.AuthorsDao
import com.firdavs.persianliterature.author.db.model.AuthorEntity

@Database(
    entities = [
        AuthorEntity::class
    ],
    version = AuthorsDb.VERSION
)
abstract class AuthorsDb : RoomDatabase() {

    abstract fun getDao(): AuthorsDao

    companion object {
        const val VERSION = 1
        const val AUTHORS = "authors"
        const val DATABASE_NAME = "authorsDb.db"
    }
}
