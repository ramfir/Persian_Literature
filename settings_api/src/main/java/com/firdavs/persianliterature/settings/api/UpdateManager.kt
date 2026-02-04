package com.firdavs.persianliterature.settings.api

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

interface UpdateManager {
    suspend fun checkForUpdate(): UpdateInfo
    suspend fun startFlexibleUpdate(activity: Activity): UpdateFlowResult
    fun completeUpdate()
    fun observeUpdateState(): StateFlow<UpdateState>
}

sealed class UpdateInfo {
    data object NoUpdateAvailable : UpdateInfo()
    data class UpdateAvailable(
        val availableVersionCode: Int,
        val updatePriority: Int
    ) : UpdateInfo()
    data object UpdateDownloaded : UpdateInfo()
}

sealed class UpdateFlowResult {
    data object Success : UpdateFlowResult()
    data object Cancelled : UpdateFlowResult()
    data class Failed(val errorCode: Int) : UpdateFlowResult()
}

data class UpdateState(
    val isUpdateAvailable: Boolean = false,
    val isUpdateDownloaded: Boolean = false,
    val availableVersionCode: Int? = null,
    val lastCheckTimestamp: Long = 0L
)
