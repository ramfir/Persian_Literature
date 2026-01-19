package com.firdavs.persianliterature.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firdavs.persianliterature.app.navigation.Navigator
import com.firdavs.persianliterature.settings.DailyNotificationWorker
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private var notificationPoemId by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle initial intent
        intent?.getStringExtra(DailyNotificationWorker.EXTRA_POEM_ID)?.let { poemId ->
            notificationPoemId = poemId
        }

        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            // Handle notification intent when poem ID changes
            LaunchedEffect(notificationPoemId) {
                notificationPoemId?.let { poemId ->
                    viewModel.setNotificationPoemId(poemId)
                    notificationPoemId = null // Reset after handling
                }
            }

            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = AppTheme.colors.background
                ) { innerPadding ->
                    Navigator(
                        state = state,
                        modifier = Modifier.padding(innerPadding),
                        onNavigationHandled = { viewModel.clearNotificationPoemId() }
                    )
                }
            }
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
