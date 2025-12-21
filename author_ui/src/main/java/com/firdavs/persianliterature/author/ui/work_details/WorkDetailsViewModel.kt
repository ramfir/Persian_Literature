package com.firdavs.persianliterature.author.ui.work_details

import androidx.lifecycle.viewModelScope
import com.firdavs.persianliterature.author_api.repository.WorksRepository
import com.firdavs.persianliterature.core.presentation.BaseViewModel
import com.firdavs.persianliterature.util.coroutines.runWithRetry
import com.firdavs.persianliterature.util.pdfdownloader.PdfDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WorkDetailsViewModel(
    private val id: String,
    private val worksRepository: WorksRepository,
    private val pdfDownloader: PdfDownloader
) : BaseViewModel<WorkDetailsUiState>(WorkDetailsUiState(null)) {
    private val downloadPdfScope = CoroutineScope(Job() + Dispatchers.IO)

    init {
        observeWork()
    }

    private fun observeWork() {
        viewModelScope.launch {
            worksRepository.getWork(id).collect { work ->
                post {
                    it.copy(work = work)
                }
                work.fileUrl?.let {
                    downloadPdf(it, work.title)
                } ?: post { it.copy(isLoading = false) }
            }
        }
    }

    private fun downloadPdf(url: String, fileName: String) {
        downloadPdfScope.launch {
            post { it.copy(isLoading = true) }
            runWithRetry(maxAttempts = 5) { tryDownloadPdf(url, fileName) }?.let {
                post { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun tryDownloadPdf(url: String, fileName: String) {
        pdfDownloader.downloadPdfFile(pdfUrl = url, fileName = fileName) { pdfFile ->
            post { it.copy(isLoading = false) }
            post { it.copy(workFile = pdfFile) }
        }
    }
}
