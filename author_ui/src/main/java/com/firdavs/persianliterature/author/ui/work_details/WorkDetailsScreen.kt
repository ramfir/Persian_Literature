package com.firdavs.persianliterature.author.ui.work_details

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.components.ProgressIndicator
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.localizedContext
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource

@Composable
fun WorkDetailsEntryPoint(
    id: String,
    onBackClick: () -> Unit
) {
    BaseEntryPoint(WorkDetailsViewModel::class, id) { state, viewModel ->
        val toastContext = localizedContext()
        LaunchedEffect(state.showAudioControlToast) {
            if (state.showAudioControlToast) {
                Toast.makeText(
                    toastContext,
                    R.string.audio_control_notification_info,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetAudioControlToastFlag()
            }
        }

        WorkDetailsScreen(
            state = state,
            onBackClick = onBackClick,
            onToggleFavourite = viewModel::onToggleFavourite,
            onPlayAudio = viewModel::onPlayAudio,
            onPauseAudio = viewModel::onPauseAudio,
            onSeekTo = viewModel::onSeekTo,
            onSkipBackward = viewModel::onSkipBackward,
            onSkipForward = viewModel::onSkipForward
        )
    }
}

@Suppress("LongMethod")
@Composable
fun WorkDetailsScreen(
    state: WorkDetailsUiState,
    onBackClick: () -> Unit,
    onToggleFavourite: (Boolean) -> Unit = {},
    onPlayAudio: () -> Unit = {},
    onPauseAudio: () -> Unit = {},
    onSeekTo: (Long) -> Unit = {},
    onSkipBackward: () -> Unit = {},
    onSkipForward: () -> Unit = {}
) {
    var isBarsVisible by remember { mutableStateOf(true) }

    BaseScreen(
        applyTopPadding = isBarsVisible,
        topBar = { drawerState, scope ->
            AnimatedVisibility(isBarsVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LocalColors.current.primary)
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
                            .align(Alignment.Center)
                            .padding(horizontal = 56.dp),
                        text = state.work?.title ?: "",
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
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
            }
        },
        mainContent = {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedVisibility(isBarsVisible) {
                    state.work?.description?.let { description ->
                        if (description.isNotBlank()) {
                            Text(
                                text = description,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalColors.current.onSurface
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.isDownloadingPdf) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            LinearProgressIndicator(
                                progress = { state.pdfDownloadProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp),
                                color = LocalColors.current.primary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(
                                    R.string.downloading,
                                    (state.pdfDownloadProgress * FULL_PERCENT).toInt()
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else if (state.work != null) {
                        // Show PDF viewer with tap-to-toggle
                        state.workFile?.let { workFile ->
                            PdfRendererViewCompose(
                                modifier = Modifier
                                    .fillMaxSize(),
                                source = PdfSource.LocalFile(workFile)
                            )
                            IconButton(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .background(
                                        color = LocalColors.current.primary,
                                        shape = CircleShape
                                    )
                                    .align(Alignment.BottomStart),
                                onClick = { isBarsVisible = !isBarsVisible }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_hide),
                                    contentDescription = null
                                )
                            }
                        } ?: Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            H3Text(text = stringResource(R.string.no_works_found))
                        }
                    }
                }
            }
        },
        footerContent = if (state.work?.audioUrl != null) {
            {
                AnimatedVisibility(isBarsVisible) {
                    AudioControlsSection(
                        modifier = Modifier,
                        state = state,
                        onPlayAudio = onPlayAudio,
                        onPauseAudio = onPauseAudio,
                        onSeekTo = onSeekTo,
                        onSkipBackward = onSkipBackward,
                        onSkipForward = onSkipForward
                    )
                }
            }
        } else {
            null
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("MagicNumber", "LongMethod")
@Composable
fun AudioControlsSection(
    modifier: Modifier = Modifier,
    state: WorkDetailsUiState,
    onPlayAudio: () -> Unit,
    onPauseAudio: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSkipBackward: () -> Unit,
    onSkipForward: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        // Audio controls - supports streaming with automatic caching
        when {
            state.work?.audioUrl != null -> {
                if (state.playbackState.isPreparing && !state.hasCompletedInitialPreparation) {
                    // Show loading indicator only during initial preparation
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
                                colors = SliderDefaults.colors(
                                    thumbColor = LocalColors.current.primary,
                                    activeTrackColor = LocalColors.current.primary,
                                    inactiveTrackColor = LocalColors.current.primary.copy(alpha = 0.3f)
                                ),
                                track = { sliderState ->
                                    SliderDefaults.Track(
                                        sliderState = sliderState,
                                        colors = SliderDefaults.colors(
                                            thumbColor = LocalColors.current.primary,
                                            activeTrackColor = LocalColors.current.primary,
                                            inactiveTrackColor = LocalColors.current.primary.copy(alpha = 0.3f)
                                        ),
                                        drawStopIndicator = null,
                                        thumbTrackGapSize = 0.dp
                                    )
                                }
                            )
                            H5Text(
                                text = formatTime(state.playbackState.duration),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        // Play/Pause/Repeat button with skip controls
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val hasReachedEnd = state.playbackState.duration > 0 &&
                                state.playbackState.currentPosition >= state.playbackState.duration - 100

                            // Skip 10 seconds backward
                            IconButton(
                                onClick = onSkipBackward
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_skip_backward_10),
                                    contentDescription = "Skip 10 seconds backward",
                                    tint = LocalColors.current.onPrimary
                                )
                            }

                            // Play/Pause/Repeat button
                            IconButton(
                                onClick = {
                                    when {
                                        hasReachedEnd -> {
                                            onSeekTo(0L)
                                            onPlayAudio()
                                        }
                                        state.playbackState.isPlaying -> onPauseAudio()
                                        else -> onPlayAudio()
                                    }
                                }
                            ) {
                                if (state.playbackState.isPlaying) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_pause),
                                        contentDescription = "Pause",
                                        tint = LocalColors.current.onPrimary
                                    )
                                } else {
                                    Icon(
                                        imageVector = when {
                                            hasReachedEnd -> Icons.Filled.Refresh
                                            else -> Icons.Filled.PlayArrow
                                        },
                                        contentDescription = null,
                                        tint = LocalColors.current.onPrimary
                                    )
                                }
                            }

                            // Skip 10 seconds forward
                            IconButton(
                                onClick = onSkipForward
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_skip_forward_10),
                                    contentDescription = "Skip 10 seconds forward",
                                    tint = LocalColors.current.onPrimary
                                )
                            }
                        }

                        // Cache progress indicator
                        val cachePercentage = (state.audioCachePercentage * FULL_PERCENT).toInt()
                        if (cachePercentage > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (cachePercentage >= FULL_PERCENT) {
                                    // Fully cached - show "Available Offline"
                                    Text(
                                        text = stringResource(R.string.available_offline),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    // Partially cached - show percentage
                                    Text(
                                        text = stringResource(R.string.cached_percentage, cachePercentage),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
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

@SuppressLint("DefaultLocale", "ImplicitDefaultLocale")
private fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / MILLIS_IN_SECOND
    val minutes = totalSeconds / SECONDS_IN_MINUTE
    val seconds = totalSeconds % SECONDS_IN_MINUTE
    return String.format("%d:%02d", minutes, seconds)
}

private const val SECONDS_IN_MINUTE = 60
private const val MILLIS_IN_SECOND = 1000
private const val FULL_PERCENT = 100
