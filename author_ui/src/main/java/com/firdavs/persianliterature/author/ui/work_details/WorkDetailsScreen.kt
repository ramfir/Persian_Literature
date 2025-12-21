package com.firdavs.persianliterature.author.ui.work_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource

@Composable
fun WorkDetailsEntryPoint(
    id: String,
    onBackClick: () -> Unit
) {
    BaseEntryPoint(WorkDetailsViewModel::class, id) { state, viewModel ->
        WorkDetailsScreen(
            state = state,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun WorkDetailsScreen(
    state: WorkDetailsUiState,
    onBackClick: () -> Unit
) {
    BaseScreen(
        topBar = { drawerState, scope ->
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                H2Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text = state.work?.title ?: "",
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                    H4Text(text = stringResource(R.string.work_loading))
                } else if (state.work != null) {
                    state.workFile?.let { workFile ->
                        PdfRendererViewCompose(
                            modifier = Modifier
                                .fillMaxSize(),
                            source = PdfSource.LocalFile(workFile)
                        )
                    } ?: H3Text(text = stringResource(R.string.no_works_found))
                }
            }
        }
    )
}
