package com.firdavs.persianliterature.author.ui.details

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author.ui.mapper.toUi
import com.firdavs.persianliterature.author_api.repository.AuthorRepository
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.util.coroutines.runWithRetry
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthorDetailsViewModel(
    private val id: String,
    private val authorRepository: AuthorRepository,
    private val worksRepository: WorksRepository,
    private val pdfDownloader: PdfDownloader
) : BaseViewModel<AuthorDetailsUiState>(AuthorDetailsUiState(null)) {

    private val downloadPdfScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        observeAuthor()
        observeWorks()
    }

    private fun observeAuthor() {
        viewModelScope.launch {
            authorRepository.getAuthor(id).collect { author ->
                post {
                    it.copy(author = author.toUi(), isLoading = false)
                }
                author.bioUrl?.let {
                    downloadPdf(it, author.name)
                } ?: post { it.copy(isLoadingFile = false) }
            }
        }
    }

    private fun observeWorks() {
        viewModelScope.launch {
            worksRepository.getWorksByAuthorId(id).collect { works ->
                post { it.copy(works = works) }
            }
        }
    }
    private fun downloadPdf(url: String, fileName: String) {
        downloadPdfScope.launch {
            post { it.copy(isLoadingFile = true) }
            runWithRetry(maxAttempts = 5) { tryDownloadPdf(url, fileName) }?.let {
                post { it.copy(isLoadingFile = false) }
            }
        }
    }

    private suspend fun tryDownloadPdf(url: String, fileName: String) {
        pdfDownloader.downloadPdfFile(pdfUrl = url, fileName = fileName) { pdfFile ->
            post { it.copy(isLoadingFile = false) }
            post { it.copy(bioFile = pdfFile) }
        }
    }

    fun onChapterClick(chapter: Chapter) {
        post { it.copy(chapter = chapter) }
    }

    companion object {
        private const val TAG = "AuthorDetailsViewModel"
    }
}
