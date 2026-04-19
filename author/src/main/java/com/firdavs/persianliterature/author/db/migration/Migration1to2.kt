package com.firdavs.persianliterature.author.db.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Database migration from version 1 to version 2.
 *
 * Adds audio cache-related fields to the works table:
 * - audioCacheStatus: Track caching progress (NOT_CACHED, PARTIALLY_CACHED, FULLY_CACHED)
 * - audioContentLength: Total audio file size in bytes
 * - audioCachedBytes: Currently cached bytes
 *
 * Converts old audioDownloadStatus = 'DOWNLOADED' to audioCacheStatus = 'FULLY_CACHED'
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns for cache tracking
        database.execSQL(
            "ALTER TABLE works ADD COLUMN audioCacheStatus TEXT NOT NULL DEFAULT 'NOT_CACHED'"
        )
        database.execSQL(
            "ALTER TABLE works ADD COLUMN audioContentLength INTEGER NOT NULL DEFAULT 0"
        )
        database.execSQL(
            "ALTER TABLE works ADD COLUMN audioCachedBytes INTEGER NOT NULL DEFAULT 0"
        )

        // Migrate old DOWNLOADED status to FULLY_CACHED
        database.execSQL(
            """
            UPDATE works
            SET audioCacheStatus = 'FULLY_CACHED'
            WHERE audioDownloadStatus = 'DOWNLOADED'
            """.trimIndent()
        )
    }
}
