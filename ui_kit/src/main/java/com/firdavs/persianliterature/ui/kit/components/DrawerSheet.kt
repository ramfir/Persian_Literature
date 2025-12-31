package com.firdavs.persianliterature.ui.kit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Composable
fun DrawerSheet(
    chapters: List<Chapter>,
    onChapterClick: (Chapter) -> Unit
) {
    val colors = LocalColors.current
    ModalDrawerSheet(
        drawerContainerColor = colors.background
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters) { chapter ->
                H4Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onChapterClick(chapter) }
                        .background(color = colors.primary, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    res = chapter.titleRes
                )
            }
        }
    }
}
