package com.firdavs.persianliterature.author.ui.model

data class AuthorUiModel(
    val id: String,
    val photoUrl: String?,
    val fullName: String,
    val birthDate: String,
    val deathDate: String,
    val bioUrl: String? = null
)
