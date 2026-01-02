package com.firdavs.persianliterature.author_api.model

data class Work(
    val id: String,
    val authorId: String,
    val title: String,
    val publishYear: String,
    val fileUrl: String?,
    val isFavourite: Boolean = false
)
