package com.firdavs.persianliterature.author_api.model

data class Author(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val born: String,
    val died: String,
    val place: String,
    val bioUrl: String?
)
