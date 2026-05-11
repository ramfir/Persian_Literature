package com.firdavs.persianliterature.author.ui.all_works

import com.firdavs.persianliterature.author_api.model.AuthorWithWorks
import com.firdavs.persianliterature.author_api.model.Work

data class AuthorWorksGroup(
    val authorId: String,
    val authorName: String,
    val works: List<Work>
)

fun List<AuthorWithWorks>.toGroups(): List<AuthorWorksGroup> = map { authorWithWorks ->
    AuthorWorksGroup(
        authorId = authorWithWorks.authorId,
        authorName = authorWithWorks.authorName,
        works = authorWithWorks.works
    )
}
