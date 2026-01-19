package com.firdavs.persianliterature.author.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Add description column to works table
        db.execSQL("ALTER TABLE works ADD COLUMN description TEXT")
    }
}
