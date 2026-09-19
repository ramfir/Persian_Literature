package com.firdavs.persianliterature.author.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE works ADD COLUMN textUrl TEXT DEFAULT NULL"
        )
        database.execSQL(
            "ALTER TABLE works ADD COLUMN lastReadTextPosition INTEGER NOT NULL DEFAULT 0"
        )
    }
}
