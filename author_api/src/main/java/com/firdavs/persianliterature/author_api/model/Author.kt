package com.firdavs.persianliterature.author_api.model

data class Author(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val birthDate: String,
    val deathDate: String,
    val birthPlace: String
)
