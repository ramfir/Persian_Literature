package com.firdavs.persianliterature.author_ui.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.firdavs.persianliterature.ui_kit.BaseEntryPoint

@Composable
fun AuthorsListEntryPoint() {
    BaseEntryPoint(AuthorsListViewModel::class) { state, viewModel ->
        AuthorsListScreen(
            state = state
        )
    }
}

@Composable
private fun AuthorsListScreen(
    state: AuthorsListUiState
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Authors List"
        )
    }
}
