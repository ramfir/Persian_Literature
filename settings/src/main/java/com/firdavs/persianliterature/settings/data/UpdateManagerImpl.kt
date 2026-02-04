package com.firdavs.persianliterature.settings.data

import android.app.Activity
import android.content.SharedPreferences
import androidx.core.content.edit
import com.firdavs.persianliterature.settings.api.UpdateFlowResult
import com.firdavs.persianliterature.settings.api.UpdateInfo
import com.firdavs.persianliterature.settings.api.UpdateManager
import com.firdavs.persianliterature.settings.api.UpdateState
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UpdateManagerImpl(
    private val appUpdateManager: AppUpdateManager,
    private val sharedPreferences: SharedPreferences
) : UpdateManager {

    private val _updateState = MutableStateFlow(UpdateState())
    private val installStateListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADED -> {
                _updateState.value = _updateState.value.copy(isUpdateDownloaded = true)
            }
            InstallStatus.DOWNLOADING -> {
                // Optionally update download progress
            }
            InstallStatus.FAILED -> {
                // Update failed, reset state
                _updateState.value = _updateState.value.copy(
                    isUpdateAvailable = false,
                    isUpdateDownloaded = false
                )
            }
            else -> { /* Other states */ }
        }
    }

    init {
        appUpdateManager.registerListener(installStateListener)
    }

    override suspend fun checkForUpdate(): UpdateInfo {
        return try {
            // Check throttle - only check once per 24 hours
            val lastCheckTimestamp = sharedPreferences.getLong(KEY_LAST_UPDATE_CHECK, 0L)
            val currentTime = System.currentTimeMillis()
            val timeSinceLastCheck = currentTime - lastCheckTimestamp

            if (timeSinceLastCheck < THROTTLE_DURATION_MS && lastCheckTimestamp != 0L) {
                // Return cached state if within throttle window
                return if (_updateState.value.isUpdateDownloaded) {
                    UpdateInfo.UpdateDownloaded
                } else if (_updateState.value.isUpdateAvailable) {
                    UpdateInfo.UpdateAvailable(
                        availableVersionCode = _updateState.value.availableVersionCode ?: 0,
                        updatePriority = 0
                    )
                } else {
                    UpdateInfo.NoUpdateAvailable
                }
            }

            // Perform actual check
            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

            // Update last check timestamp
            sharedPreferences.edit {
                putLong(KEY_LAST_UPDATE_CHECK, currentTime)
            }

            when {
                appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED -> {
                    _updateState.value = _updateState.value.copy(
                        isUpdateDownloaded = true,
                        lastCheckTimestamp = currentTime
                    )
                    UpdateInfo.UpdateDownloaded
                }
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                    val versionCode = appUpdateInfo.availableVersionCode()
                    val priority = appUpdateInfo.updatePriority()

                    _updateState.value = _updateState.value.copy(
                        isUpdateAvailable = true,
                        availableVersionCode = versionCode,
                        lastCheckTimestamp = currentTime
                    )

                    UpdateInfo.UpdateAvailable(
                        availableVersionCode = versionCode,
                        updatePriority = priority
                    )
                }
                else -> {
                    _updateState.value = _updateState.value.copy(
                        isUpdateAvailable = false,
                        isUpdateDownloaded = false,
                        availableVersionCode = null,
                        lastCheckTimestamp = currentTime
                    )
                    UpdateInfo.NoUpdateAvailable
                }
            }
        } catch (e: Exception) {
            // Log error but don't crash
            e.printStackTrace()
            UpdateInfo.NoUpdateAvailable
        }
    }

    override suspend fun startFlexibleUpdate(activity: Activity): UpdateFlowResult {
        return try {
            val appUpdateInfo = appUpdateManager.appUpdateInfo.await()

            if (appUpdateInfo.updateAvailability() != UpdateAvailability.UPDATE_AVAILABLE ||
                !appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                return UpdateFlowResult.Failed(-1)
            }

            suspendCoroutine { continuation ->
                val updateOptions = AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    activity,
                    updateOptions,
                    UPDATE_REQUEST_CODE
                )

                // For flexible updates, we consider the flow successful when started
                // The actual download happens in background
                continuation.resume(UpdateFlowResult.Success)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            UpdateFlowResult.Failed(-1)
        }
    }

    override fun completeUpdate() {
        appUpdateManager.completeUpdate()
    }

    override fun observeUpdateState(): StateFlow<UpdateState> {
        return _updateState.asStateFlow()
    }

    fun cleanup() {
        appUpdateManager.unregisterListener(installStateListener)
    }

    companion object {
        private const val KEY_LAST_UPDATE_CHECK = "last_update_check_timestamp"
        private const val THROTTLE_DURATION_MS = 24 * 60 * 60 * 1000L // 24 hours
        const val UPDATE_REQUEST_CODE = 1002
    }
}
