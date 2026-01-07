package com.firdavs.persianliterature.author.ui.work_details

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author_api.model.AudioDownloadStatus
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
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
            onBackClick = onBackClick,
            onToggleFavourite = viewModel::onToggleFavourite,
            onDownloadAudio = viewModel::onDownloadAudio,
            onPlayAudio = viewModel::onPlayAudio,
            onPauseAudio = viewModel::onPauseAudio,
            onStopAudio = viewModel::onStopAudio,
            onSeekTo = viewModel::onSeekTo
        )
    }
}

@Composable
fun WorkDetailsScreen(
    state: WorkDetailsUiState,
    onBackClick: () -> Unit,
    onToggleFavourite: (Boolean) -> Unit = {},
    onDownloadAudio: () -> Unit = {},
    onPlayAudio: () -> Unit = {},
    onPauseAudio: () -> Unit = {},
    onStopAudio: () -> Unit = {},
    onSeekTo: (Long) -> Unit = {}
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
                state.work?.let { work ->
                    IconButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                        onClick = { onToggleFavourite(!work.isFavourite) }
                    ) {
                        Icon(
                            imageVector = if (work.isFavourite) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            contentDescription = null,
                            tint = if (work.isFavourite) {
                                LocalColors.current.primary
                            } else {
                                LocalColors.current.onPrimary
                            }
                        )
                    }
                }
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
                    ProgressIndicator()
                    H4Text(text = stringResource(R.string.work_loading))
                } else if (state.work != null) {
                    // Show audio controls if work has audio
                    state.work.audioUrl?.let {
                        AudioControlsSection(
                            state = state,
                            onDownloadAudio = onDownloadAudio,
                            onPlayAudio = onPlayAudio,
                            onPauseAudio = onPauseAudio,
                            onStopAudio = onStopAudio,
                            onSeekTo = onSeekTo
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Show PDF viewer
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

@Composable
fun AudioControlsSection(
    state: WorkDetailsUiState,
    onDownloadAudio: () -> Unit,
    onPlayAudio: () -> Unit,
    onPauseAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onSeekTo: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.current.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            // Download button or status
            when {
                state.work?.audioDownloadStatus == AudioDownloadStatus.NOT_DOWNLOADED -> {
                    Button(
                        onClick = onDownloadAudio,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.download_audio))
                    }
                }
                state.isDownloadingAudio -> {
                    Column(horizontalAlignment = CenterHorizontally) {
                        LinearProgressIndicator(
                            progress = { state.audioDownloadProgress },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.downloading_audio,
                                (state.audioDownloadProgress * FULL_PERCENT).toInt()
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                state.work?.audioDownloadStatus == AudioDownloadStatus.DOWNLOADED -> {
                    if (state.playbackState.isPreparing) {
                        // Show loading indicator while preparing
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = CenterHorizontally
                        ) {
                            ProgressIndicator()
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Progress slider with time display
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                H5Text(
                                    text = formatTime(state.playbackState.currentPosition),
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                                Slider(
                                    value = state.playbackState.currentPosition.toFloat(),
                                    onValueChange = { onSeekTo(it.toLong()) },
                                    valueRange = 0f..state.playbackState.duration.toFloat().coerceAtLeast(1f),
                                    modifier = Modifier.weight(1f),
                                    colors = SliderDefaults.colors()
                                )
                                H5Text(
                                    text = formatTime(state.playbackState.duration),
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }

                            // Play/Pause button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = if (state.playbackState.isPlaying) onPauseAudio else onPlayAudio
                                ) {
                                    Icon(
                                        imageVector = if (state.playbackState.isPlaying) {
                                            Icons.Filled.Favorite
                                        } else {
                                            Icons.Filled.PlayArrow
                                        },
                                        contentDescription = if (state.playbackState.isPlaying) "Pause" else "Play",
                                        tint = LocalColors.current.onPrimary
                                    )
                                }
                            }

                            // Show playback status
                            if (state.playbackState.isPlaying) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.now_playing),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LocalColors.current.primary
                                    )
                                }
                            }
                        }
                    }
                }
                state.work?.audioDownloadStatus == AudioDownloadStatus.FAILED -> {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.download_failed),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = onDownloadAudio) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.retry_download))
                        }
                    }
                }
            }

            // Error display
            state.audioDownloadError?.let { error ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
private fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / MILLIS_IN_SECOND
    val minutes = totalSeconds / SECONDS_IN_MINUTE
    val seconds = totalSeconds % SECONDS_IN_MINUTE
    return String.format("%d:%02d", minutes, seconds)
}

private const val SECONDS_IN_MINUTE = 60
private const val MILLIS_IN_SECOND = 1000
private const val FULL_PERCENT = 100
