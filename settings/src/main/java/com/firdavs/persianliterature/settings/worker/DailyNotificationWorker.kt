package com.firdavs.persianliterature.settings.worker

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.firdavs.persianliterature.author.db.dao.PoemsDao
import com.firdavs.persianliterature.settings.R
import com.firdavs.persianliterature.settings.api.NotificationManager as NotificationManagerApi

class DailyNotificationWorker(
    private val context: Context,
    params: WorkerParameters,
    private val notificationManager: NotificationManagerApi,
    private val poemsDao: PoemsDao
) : CoroutineWorker(context, params) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        // Check if notifications are still enabled
        if (!notificationManager.isNotificationEnabled(context)) {
            return Result.success()
        }

        // Check runtime permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.failure()
            }
        }

        // Show notification
        showNotification()

        // Reschedule for tomorrow
        notificationManager.scheduleNotification(context)

        return Result.success()
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private suspend fun showNotification() {
        // Get a random poem from the database
        val randomPoem = poemsDao.getRandomPoem()

        // Create explicit intent to MainActivity
        val intent = Intent().apply {
            setClassName(context.packageName, "com.firdavs.persianliterature.app.ui.MainActivity")
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // Add poem ID as extra so MainActivity can navigate to it
            randomPoem?.id?.let { putExtra(EXTRA_POEM_ID, it) }
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            pendingIntentFlags
        )

        // Use random poem data if available, otherwise use default strings
        val notificationTitle = randomPoem?.author ?: context.getString(R.string.notification_title)
        val notificationText = randomPoem?.text?.take(MAX_TEXT_LENGTH)?.plus(
            if ((randomPoem?.text?.length ?: 0) > MAX_TEXT_LENGTH) "..." else ""
        ) ?: context.getString(R.string.notification_text)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.firdavs.persianliterature.core.R.drawable.ic_notification)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(notificationText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "daily_poem_channel"
        const val EXTRA_POEM_ID = "poem_id"
        private const val NOTIFICATION_ID = 1001
        private const val MAX_TEXT_LENGTH = 100
    }
}
