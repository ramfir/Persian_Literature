package com.firdavs.persianliterature.author.ui.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.author.ui.R
import com.firdavs.persianliterature.author.ui.list.AuthorItem
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.ui.kit.BaseEntryPoint
import com.firdavs.persianliterature.ui.kit.BaseScreen
import com.firdavs.persianliterature.ui.kit.H2Text
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.theme.AppPreviewTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.rajat.pdfviewer.PdfRendererView
import com.rajat.pdfviewer.compose.PdfRendererViewCompose
import com.rajat.pdfviewer.util.PdfSource
import java.io.File

@Composable
fun AuthorDetailsEntryPoint(
    id: String,
    onBackClick: () -> Unit,
    onWorkClick: (String) -> Unit
) {
    BaseEntryPoint(AuthorDetailsViewModel::class, id) { state, viewModel ->
        AuthorDetailsScreen(
            state = state,
            onWorkClick = onWorkClick,
            onChapterClick = viewModel::onChapterClick,
            onBackClick = onBackClick
        )
    }
}

@Composable
fun AuthorDetailsScreen(
    state: AuthorDetailsUiState,
    onChapterClick: (Chapter) -> Unit,
    onWorkClick: (String) -> Unit,
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
                    text = stringResource(state.chapter.getStringRes()),
                    textAlign = TextAlign.Center
                )
            }
        },
        mainContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                } else if (state.author != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = CenterHorizontally
                    ) {
                        when (state.chapter) {
                            Chapter.Bio -> {
                                BioChapter(
                                    author = state.author,
                                    isLoadingFile = state.isLoadingFile,
                                    bioFile = state.bioFile
                                )
                            }
                            Chapter.Works -> {
                                WorksChapter(
                                    works = state.works,
                                    onWorkClick = onWorkClick
                                )
                            }
                        }
                    }
                }
            }
        },
        footerContent = {
            AuthorDetailsFooter(
                onChapterClick = onChapterClick
            )
        }
    )
}

@Composable
private fun BioChapter(
    author: AuthorUiModel,
    isLoadingFile: Boolean,
    bioFile: File?
) {
    var isHeaderVisible by remember { mutableStateOf(true) }
    AnimatedVisibility(isHeaderVisible) {
        Column(
            horizontalAlignment = CenterHorizontally
        ) {
            AuthorItem(
                author = author,
                onAuthorClick = {}
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        if (isLoadingFile) {
            CircularProgressIndicator()
            H4Text(text = stringResource(R.string.bio_loading))
        } else {
            bioFile?.let { bioFile ->
                PdfRendererViewCompose(
                    modifier = Modifier
                        .fillMaxSize(),
                    source = PdfSource.LocalFile(bioFile),
                    statusCallBack = object : PdfRendererView.StatusCallBack {
                        override fun onPageChanged(currentPage: Int, totalPage: Int) {
                            super.onPageChanged(currentPage, totalPage)
                            if (currentPage == 1) {
                                isHeaderVisible = true
                            } else {
                                isHeaderVisible = false
                            }
                        }
                    }
                )
            } ?: H3Text(text = stringResource(R.string.no_bio_found))
        }
    }
}

@Composable
private fun WorksChapter(works: List<Work>, onWorkClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (works.isEmpty()) {
            Spacer(Modifier.weight(1f))
            H3Text(
                modifier = Modifier
                    .fillMaxWidth(),
                res = R.string.no_works_found,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.weight(1f))
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(works) { work ->
                    WorkItem(work, onWorkClick = onWorkClick)
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 1.dp,
                        color = LocalColors.current.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkItem(
    work: Work,
    onWorkClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onWorkClick(work.id)
            })
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        H4Text(
            text = work.title
        )
        H5Text(
            text = stringResource(R.string.published_at, work.publishYear)
        )
    }
}

@Composable
private fun AuthorDetailsFooter(
    onChapterClick: (Chapter) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                onChapterClick.invoke(Chapter.Bio)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = null,
                tint = LocalColors.current.onPrimary
            )
        }
        IconButton(
            modifier = Modifier
                .weight(1f),
            onClick = {
                onChapterClick.invoke(Chapter.Works)
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = LocalColors.current.onPrimary
            )
        }
    }
}

@Preview
@Composable
private fun AuthorDetailsScreenPreview(
    @PreviewParameter(AuthorDetailsStateProvider::class) state: AuthorDetailsUiState
) {
    AppPreviewTheme {
        AuthorDetailsScreen(
            state = state,
            onChapterClick = {},
            onWorkClick = {},
            onBackClick = {}
        )
    }
}
