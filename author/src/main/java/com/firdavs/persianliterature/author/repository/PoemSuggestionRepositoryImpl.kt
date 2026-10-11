package com.firdavs.persianliterature.author.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.firdavs.persianliterature.author_api.manager.AnonymousAuthManager
import com.firdavs.persianliterature.author_api.model.PoemSuggestion
import com.firdavs.persianliterature.author_api.repository.PoemSuggestionRepository
import com.firdavs.persianliterature.author_api.repository.PoemSuggestionRepository.Companion.DAILY_LIMIT
import com.firdavs.persianliterature.author_api.repository.SuggestionRejectedException
import com.firdavs.persianliterature.settings.api.LanguageManager
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class PoemSuggestionRepositoryImpl(
    private val anonymousAuthManager: AnonymousAuthManager,
    private val languageManager: LanguageManager,
    private val context: Context
) : PoemSuggestionRepository {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun submitSuggestion(suggestion: PoemSuggestion) {
        val data = hashMapOf(
            "title" to suggestion.title.trim(),
            "text" to suggestion.text.trim(),
            "author" to suggestion.author.trim(),
            "suggesterName" to suggestion.suggesterName.trim(),
            "lang" to languageManager.getSavedLanguage(context).firebaseCode,
            "status" to STATUS_PENDING,
            "createdAt" to FieldValue.serverTimestamp(),
            "appVersion" to getAppVersion(),
            "uid" to anonymousAuthManager.getUid()
        )
        try {
            Firebase.firestore.collection(COLLECTION).add(data).await()
        } catch (e: FirebaseFirestoreException) {
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED) {
                Log.w(TAG, "Suggestion rejected by security rules", e)
                throw SuggestionRejectedException(e)
            }
            throw e
        }
        countSubmission()
    }

    override fun getRemainingSuggestionsToday(): Int {
        return (DAILY_LIMIT - getTodayCount()).coerceAtLeast(0)
    }

    private fun getTodayCount(): Int {
        val today = LocalDate.now().toEpochDay()
        return if (prefs.getLong(KEY_DAY, 0L) == today) prefs.getInt(KEY_COUNT, 0) else 0
    }

    private fun countSubmission() {
        val count = getTodayCount() + 1
        prefs.edit {
            putLong(KEY_DAY, LocalDate.now().toEpochDay())
            putInt(KEY_COUNT, count)
        }
    }

    @Suppress("DEPRECATION", "SwallowedException")
    private fun getAppVersion(): String {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName.orEmpty()
        } catch (e: Exception) {
            ""
        }
    }

    private companion object {
        const val TAG = "PoemSuggestionRepo"
        const val COLLECTION = "poem_suggestions"
        const val STATUS_PENDING = "pending"
        const val PREF_NAME = "poem_suggestions_prefs"
        const val KEY_DAY = "quota_day"
        const val KEY_COUNT = "quota_count"
    }
}
