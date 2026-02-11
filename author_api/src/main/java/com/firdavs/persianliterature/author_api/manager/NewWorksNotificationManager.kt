package com.firdavs.persianliterature.author_api.manager

import com.firdavs.persianliterature.author_api.model.NewWorkItem

interface NewWorksNotificationManager {
    suspend fun checkForNewWorks(): List<NewWorkItem>
}
