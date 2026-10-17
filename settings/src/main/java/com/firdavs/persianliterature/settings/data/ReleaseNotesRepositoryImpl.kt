package com.firdavs.persianliterature.settings.data

import android.content.Context
import android.util.Log
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.firdavs.persianliterature.settings.api.ReleaseNotes
import com.firdavs.persianliterature.settings.api.ReleaseNotesRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

/** Reads release notes from Firestore collection `release_notes_<lang>`. */
class ReleaseNotesRepositoryImpl(
    private val languageManager: LanguageManager,
    private val context: Context
) : ReleaseNotesRepository {

    override suspend fun getReleaseNotes(fromVersionCode: Int, toVersionCode: Int): List<ReleaseNotes> {
        if (toVersionCode <= fromVersionCode) return emptyList()
        return try {
            val lang = languageManager.getSavedLanguage(context).firebaseCode
            val snapshot = Firebase.firestore.collection("release_notes_$lang")
                .whereGreaterThan(FIELD_VERSION_CODE, fromVersionCode)
                .whereLessThanOrEqualTo(FIELD_VERSION_CODE, toVersionCode)
                .orderBy(FIELD_VERSION_CODE, Query.Direction.DESCENDING)
                .get(Source.SERVER)
                .await()

            snapshot.documents.mapNotNull { doc ->
                val versionCode = doc.getLong(FIELD_VERSION_CODE)?.toInt() ?: return@mapNotNull null
                val items = (doc.get(FIELD_ITEMS) as? List<*>)
                    ?.filterIsInstance<String>()
                    ?.filter { it.isNotBlank() }
                    .orEmpty()
                if (items.isEmpty()) return@mapNotNull null

                ReleaseNotes(
                    versionCode = versionCode,
                    versionName = doc.getString(FIELD_VERSION_NAME).orEmpty(),
                    items = items
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch release notes", e)
            emptyList()
        }
    }

    companion object {
        private const val TAG = "ReleaseNotesRepository"
        private const val FIELD_VERSION_CODE = "versionCode"
        private const val FIELD_VERSION_NAME = "versionName"
        private const val FIELD_ITEMS = "items"
    }
}
