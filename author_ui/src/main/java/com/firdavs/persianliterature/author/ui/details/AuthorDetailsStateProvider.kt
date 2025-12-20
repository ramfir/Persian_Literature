package com.firdavs.persianliterature.author.ui.details

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.firdavs.persianliterature.author.ui.model.AuthorUiModel

class AuthorDetailsStateProvider : PreviewParameterProvider<AuthorDetailsUiState> {
    override val values: Sequence<AuthorDetailsUiState> = sequenceOf(
        AuthorDetailsUiState(null),
        AuthorDetailsUiState(
            id = null,
            isLoading = false,
            author = AuthorUiModel(
                id = "hafiz",
                photoUrl = "https://firebasestorage.googleapis.com/v0/b/persian-literature." +
                    "firebasestorage.app/o/hafiz.jpeg?alt=media&token=3c7a243d-1a25-417f-a" +
                    "173-8e730197b561",
                fullName = "Hafiz Sherozi",
                birthDate = "12.12.1222",
                deathDate = "12.12.1292"
            )
        )
    )
}
