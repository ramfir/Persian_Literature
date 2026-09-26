package com.firdavs.persianliterature.author.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE works ADD COLUMN lastReadPage INTEGER NOT NULL DEFAULT 0"
        )
    }
}
