package com.firdavs.persianliterature.settings

import android.content.Context
import androidx.core.content.edit
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.firdavs.persianliterature.settings.api.NotificationManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationManagerImpl : NotificationManager {

    override fun isNotificationEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, false)
    }

    override fun setNotificationEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(KEY_NOTIFICATIONS_ENABLED, enabled) }
    }

    override fun scheduleNotification(context: Context) {
        val delay = calculateDelayToNextTenAM()

        val workRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun cancelNotification(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }

    private fun calculateDelayToNextTenAM(): Long {
        //return TimeUnit.MINUTES.toMillis(1)
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(currentTime)) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return targetTime.timeInMillis - currentTime.timeInMillis
    }

    companion object {
        private const val PREF_NAME = "language_preferences"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val WORK_NAME = "daily_notification"
        private const val WORK_TAG = "daily_notification_work"
    }
}
