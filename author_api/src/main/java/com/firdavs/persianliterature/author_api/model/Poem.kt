package com.firdavs.persianliterature.author_api.model

data class Poem(
    val id: String,
    val title: String,
    val text: String,
    val author: String,
    val isFavourite: Boolean = false
)
