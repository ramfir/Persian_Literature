package com.firdavs.persianliterature.author_api.model

data class NewWorkItem(
    val id: String,
    val title: String,
    val author: String,
    val language: String // "en", "ru", or "tg"
)
