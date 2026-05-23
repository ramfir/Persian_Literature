package com.firdavs.persianliterature.poem_of_day

import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.T1Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.components.DrawerSheet
import com.firdavs.persianliterature.ui.kit.components.buttons.PrimaryButton
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.firdavs.persianliterature.core.R as UiR

@Composable
fun PoemOfDayEntryPoint(
    poemId: String?,
    onChapterClick: (Chapter) -> Unit
) {
    BaseEntryPoint(PoemOfDayViewModel::class, poemId) { state, viewModel ->
        PoemOfDayScreen(
            state = state,
            onChapterClick = onChapterClick,
            onNewPoemClick = viewModel::onNewPoemClick,
            onPreviousPoemClick = viewModel::onPreviousPoemClick,
            onToggleFavourite = viewModel::onToggleFavourite
        )
    }
}

@Composable
private fun PoemOfDayScreen(
    state: PoemOfDayUiState,
    onChapterClick: (Chapter) -> Unit,
    onNewPoemClick: () -> Unit,
    onPreviousPoemClick: () -> Unit,
    onToggleFavourite: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    BaseScreen(
        modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            },
        drawerContent = {
            DrawerSheet(
                chapters = state.chapters,
                currentChapter = Chapter.PoemOfDay,
                onChapterClick = onChapterClick
            )
        },
        topBar = { drawerState, scope ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LocalColors.current.primary)
                    .padding(horizontal = 8.dp)
            ) {
                IconButton(
                    onClick = { scope.launch { drawerState.open() } }
                ) {
                    Icon(Icons.Default.Menu, "Open drawer")
                }
                H3Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp),
                    text = stringResource(UiR.string.poem_of_day),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (state.poem != null) {
                    Row(
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                        val file = File(context.cacheDir, "poem_of_day.png")
                                        withContext(Dispatchers.IO) {
                                            file.outputStream().use { out ->
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                                            }
                                        }
                                        val uri = FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.fileprovider",
                                            file
                                        )
                                        val intent = Intent(Intent.ACTION_SEND).apply {
                                            type = "image/png"
                                            putExtra(Intent.EXTRA_STREAM, uri)
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                        context.startActivity(Intent.createChooser(intent, null))
                                    } catch (e: Exception) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(
                                                context,
                                                context.getString(R.string.share_poem_error),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = stringResource(R.string.share_poem)
                            )
                        }
                        IconButton(
                            onClick = onToggleFavourite
                        ) {
                            Icon(
                                imageVector = if (state.poem.isFavourite) {
                                    Icons.Filled.Favorite
                                } else {
                                    Icons.Outlined.FavoriteBorder
                                },
                                contentDescription = "Toggle favourite",
                                tint = LocalColors.current.onPrimary
                            )
                        }
                    }
                }
            }
        },
        mainContent = {
            if (state.isLoading && state.poem == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.poem != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val scrollState = rememberScrollState()
                    LaunchedEffect(state.poem) {
                        scrollState.scrollTo(0)
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = LocalColors.current.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            H2Text(
                                text = state.poem.title,
                                textAlign = TextAlign.Center,
                                color = LocalColors.current.primary
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            T1Text(
                                text = state.poem.text,
                                textAlign = TextAlign.Center,
                                color = LocalColors.current.onSurface
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            T2Text(
                                text = "— ${state.poem.author}",
                                textAlign = TextAlign.End,
                                color = LocalColors.current.onSurfaceVariant,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.previous_poem),
                            enabled = !state.isLoading && state.allPoems.isNotEmpty(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            onClick = onPreviousPoemClick
                        )
                        PrimaryButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.new_poem),
                            enabled = !state.isLoading && state.allPoems.isNotEmpty(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 14.sp,
                            onClick = onNewPoemClick
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    T1Text(
                        text = stringResource(R.string.no_poems_available),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}
