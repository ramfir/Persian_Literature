package com.firdavs.persianliterature.author.ui.work_details

import com.firdavs.persianliterature.author_api.model.Work
import com.firdavs.persianliterature.core.presentation.UiState
import java.io.File

data class WorkDetailsUiState(
    val id: String?,
    val work: Work? = null,
    val workFile: File? = null,
    val isLoading: Boolean = true
) : UiState()
