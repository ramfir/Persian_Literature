package com.firdavs.persianliterature.author_api.model

data class PoemSuggestion(
    val title: String,
    val text: String,
    val author: String,
    val suggesterName: String
)
