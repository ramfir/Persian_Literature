package com.firdavs.persianliterature.author_api.model

data class AuthorWithWorks(
    val authorId: String,
    val authorName: String,
    val works: List<Work>
)
