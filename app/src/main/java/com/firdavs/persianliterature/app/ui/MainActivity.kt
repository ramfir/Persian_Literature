package com.firdavs.persianliterature.app.ui

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firdavs.persianliterature.app.navigation.Navigator
import com.firdavs.persianliterature.settings.api.LocaleHolder
import com.firdavs.persianliterature.settings.worker.DailyNotificationWorker
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalAppLocale
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    private var notificationPoemId by mutableStateOf<String?>(null)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before calling super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // Handle initial intent
        intent?.getStringExtra(DailyNotificationWorker.EXTRA_POEM_ID)?.let { poemId ->
            notificationPoemId = poemId
        }

        setContent {
            viewModel = koinViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            // Get LocaleHolder (LanguageManager) from Koin
            val localeHolder = koinInject<LocaleHolder>()
            val currentLocale by localeHolder.currentLocale.collectAsStateWithLifecycle(
                initialValue = localeHolder.systemLocale
            )

            // Permission launcher for notifications
            val notificationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                viewModel.onNotificationPermissionResult(isGranted)
            }

            // Handle notification permission request
            LaunchedEffect(state.requestNotificationPermission) {
                if (state.requestNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else if (
                    state.requestNotificationPermission && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                ) {
                    // For Android 12 and below, permission is granted at install time
                    viewModel.onNotificationPermissionResult(true)
                }
            }

            // Handle notification intent when poem ID changes
            LaunchedEffect(notificationPoemId) {
                notificationPoemId?.let { poemId ->
                    viewModel.setNotificationPoemId(poemId)
                    notificationPoemId = null // Reset after handling
                }
            }

            AppTheme {
                CompositionLocalProvider(LocalAppLocale provides currentLocale) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        containerColor = AppTheme.colors.background
                    ) { innerPadding ->
                        Navigator(
                            state = state,
                            modifier = Modifier.padding(innerPadding),
                            onNavigationHandled = {
                                viewModel.clearNotificationPoemId()
                                viewModel.clearNavigationWorkId()
                            }
                        )

                        if (state.showLanguageSelectionDialog) {
                            LanguageSelectionDialog(
                                onLanguageSelected = { language ->
                                    viewModel.onLanguageSelected(language)
                                }
                            )
                        }

                        if (state.showNotificationPermissionDialog) {
                            NotificationPermissionDialog(
                                onAllowClicked = { viewModel.onNotificationPermissionAllowClicked() },
                                onSkipClicked = { viewModel.onNotificationPermissionSkipClicked() }
                            )
                        }

                        if (state.showUpdateDialog) {
                            UpdateDialog(
                                onUpdate = { viewModel.onUpdateClicked(this@MainActivity) },
                                onDismiss = { viewModel.onUpdateDialogDismissed() }
                            )
                        }

                        if (state.showInstallPrompt) {
                            UpdateInstallPrompt(
                                onInstall = { viewModel.onInstallUpdateClicked() },
                                onDismiss = { viewModel.onInstallPromptDismissed() }
                            )
                        }

                        if (state.newWorks.isNotEmpty()) {
                            NewWorksDialog(
                                newWorks = state.newWorks,
                                onWorkClick = { workId ->
                                    viewModel.onNewWorkClicked(workId)
                                },
                                onDismiss = {
                                    viewModel.onNewWorksDialogShown()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if update was downloaded while app was in background
        if (::viewModel.isInitialized) {
            viewModel.checkIfUpdateDownloaded()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Handle notification intent when app is already running
        intent.getStringExtra(DailyNotificationWorker.EXTRA_POEM_ID)?.let { poemId ->
            notificationPoemId = poemId
        }
    }
}
