package com.firdavs.persianliterature.author.ui.all_works

import com.firdavs.persianliterature.author.ui.model.AuthorUiModel
import com.firdavs.persianliterature.author_api.model.Work

sealed class AllWorksListItem {
    data class AuthorHeader(val author: AuthorUiModel) : AllWorksListItem()
    data class WorkItem(val work: Work) : AllWorksListItem()
    data class EmptyWorksMessage(val authorId: String) : AllWorksListItem()
}

/**
 * Extension function to flatten a map of authors and their works into a single list
 * suitable for display in a LazyColumn.
 *
 * For each author:
 * - Adds an AuthorHeader item
 * - Adds WorkItem for each work, or EmptyWorksMessage if no works exist
 */
fun Map<AuthorUiModel, List<Work>>.toListItems(): List<AllWorksListItem> {
    return flatMap { (author, works) ->
        buildList {
            add(AllWorksListItem.AuthorHeader(author))
            if (works.isEmpty()) {
                add(AllWorksListItem.EmptyWorksMessage(author.id))
            } else {
                works.forEach { work ->
                    add(AllWorksListItem.WorkItem(work))
                }
            }
        }
    }
}
