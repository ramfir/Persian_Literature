package com.firdavs.persianliterature.settings.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.firdavs.persianliterature.settings.api.NotificationManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BootReceiver : BroadcastReceiver(), KoinComponent {

    private val notificationManager: NotificationManager by inject()

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule notification if it was enabled
            if (notificationManager.isNotificationEnabled(context)) {
                notificationManager.scheduleNotification(context)
            }
        }
    }
}
