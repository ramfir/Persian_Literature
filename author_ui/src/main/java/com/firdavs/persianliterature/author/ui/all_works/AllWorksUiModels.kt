package com.firdavs.persianliterature.author.ui.all_works

import com.firdavs.persianliterature.author_api.model.AuthorWithWorks

sealed class AllWorksListItem {
    data class AuthorHeader(val authorId: String, val authorName: String) : AllWorksListItem()
    data class WorkItem(val work: com.firdavs.persianliterature.author_api.model.Work) : AllWorksListItem()
    data class EmptyWorksMessage(val authorId: String) : AllWorksListItem()
}

/**
 * Extension function to flatten a list of authors with works into a single list
 * suitable for display in a LazyColumn.
 *
 * For each author:
 * - Adds an AuthorHeader item
 * - Adds WorkItem for each work, or EmptyWorksMessage if no works exist
 */
fun List<AuthorWithWorks>.toListItems(): List<AllWorksListItem> {
    return flatMap { authorWithWorks ->
        buildList {
            add(AllWorksListItem.AuthorHeader(authorWithWorks.authorId, authorWithWorks.authorName))
            if (authorWithWorks.works.isEmpty()) {
                add(AllWorksListItem.EmptyWorksMessage(authorWithWorks.authorId))
            } else {
                authorWithWorks.works.forEach { work ->
                    add(AllWorksListItem.WorkItem(work))
                }
            }
        }
    }
}
