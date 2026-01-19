package com.firdavs.persianliterature.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.firdavs.persianliterature.app.navigation.Navigator
import com.firdavs.persianliterature.settings.DailyNotificationWorker
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = koinViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            // Handle notification intent
            LaunchedEffect(Unit) {
                intent?.getStringExtra(DailyNotificationWorker.EXTRA_POEM_ID)?.let { poemId ->
                    viewModel.setNotificationPoemId(poemId)
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
}
