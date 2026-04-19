package com.firdavs.persianliterature.author.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.firdavs.persianliterature.author_api.manager.NewWorksNotificationManager
import com.firdavs.persianliterature.author_api.model.NewWorkItem
import com.google.firebase.Firebase
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class NewWorksNotificationManagerImpl(
    private val context: Context
) : NewWorksNotificationManager {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences("new_works_notification_prefs", Context.MODE_PRIVATE)
    }

    private val firestore = Firebase.firestore

    companion object {
        private const val KEY_LAST_CHECK_TIMESTAMP = "last_check_timestamp"
        private const val KEY_CACHED_WORK_IDS = "cached_work_ids"
        private const val THROTTLE_DURATION_MS = 24 * 60 * 60 * 1000L // 24 hours
    }

    override suspend fun checkForNewWorks(): List<NewWorkItem> {
        return try {
            // Check throttle - only check Firestore once per 24 hours
            val lastCheckTimestamp = prefs.getLong(KEY_LAST_CHECK_TIMESTAMP, 0L)
            val currentTime = System.currentTimeMillis()
            val timeSinceLastCheck = currentTime - lastCheckTimestamp

            if (timeSinceLastCheck < THROTTLE_DURATION_MS && lastCheckTimestamp != 0L) {
                // Within throttle window, return empty list (no new check needed)
                return emptyList()
            }

            // Perform actual check - fetch from all 3 language collections in parallel
            val allNewWorks = fetchNewWorksFromAllLanguages()

            // Update last check timestamp
            prefs.edit {
                putLong(KEY_LAST_CHECK_TIMESTAMP, currentTime)
            }

            // Get all work IDs from the fetched works
            val fetchedIds = allNewWorks.map { it.id }.toSet()
            val cachedIds = prefs.getStringSet(KEY_CACHED_WORK_IDS, emptySet()) ?: emptySet()

            // Find new work IDs
            val newWorkIds = fetchedIds - cachedIds

            if (newWorkIds.isEmpty()) {
                // Update cache even if no new works
                prefs.edit { putStringSet(KEY_CACHED_WORK_IDS, fetchedIds) }
                return emptyList()
            }

            // Filter to only include new works
            val newWorks = allNewWorks.filter { it.id in newWorkIds }

            // Update cache with full fetched list
            prefs.edit { putStringSet(KEY_CACHED_WORK_IDS, fetchedIds) }

            newWorks
        } catch (e: Exception) {
            // Log error, return empty list (don't block app launch)
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchNewWorksFromAllLanguages(): List<NewWorkItem> = coroutineScope {
        val languages = listOf("en", "ru", "tj")

        // Fetch from all 3 collections in parallel
        val deferredResults = languages.map { lang ->
            async {
                try {
                    val snapshot = firestore.collection("new_works_$lang")
                        .get(Source.SERVER)
                        .await()

                    snapshot.documents.mapNotNull { doc ->
                        try {
                            val id = doc.getString("id") ?: return@mapNotNull null
                            val title = doc.getString("title") ?: return@mapNotNull null
                            val authorName = doc.getString("author") ?: return@mapNotNull null

                            NewWorkItem(
                                id = id,
                                title = title,
                                author = authorName,
                                language = lang
                            )
                        } catch (e: Exception) {
                            null
                        }
                    }
                } catch (e: Exception) {
                    emptyList()
                }
            }
        }

        // Combine all results
        deferredResults.awaitAll().flatten()
    }
}
