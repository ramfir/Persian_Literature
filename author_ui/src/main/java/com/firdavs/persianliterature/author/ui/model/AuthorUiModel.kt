package com.firdavs.persianliterature.author.ui.model

data class AuthorUiModel(
    val id: String,
    val photoUrl: String?,
    val name: String,
    val born: String,
    val died: String,
    val bioUrl: String? = null,
    val isFavourite: Boolean = false
)
