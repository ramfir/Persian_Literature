package com.firdavs.persianliterature.settings.api

import android.content.Context

interface NotificationManager {
    fun isNotificationEnabled(context: Context): Boolean
    fun setNotificationEnabled(context: Context, enabled: Boolean)
    fun scheduleNotification(context: Context)
    fun cancelNotification(context: Context)
}
