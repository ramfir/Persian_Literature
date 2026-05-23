package com.firdavs.persianliterature.author.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE poems ADD COLUMN isFavourite INTEGER NOT NULL DEFAULT 0"
        )
    }
}
