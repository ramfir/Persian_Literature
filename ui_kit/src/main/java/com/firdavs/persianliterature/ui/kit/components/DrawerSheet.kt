package com.firdavs.persianliterature.ui.kit.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.core.model.Chapter
import com.firdavs.persianliterature.ui.kit.H4Text
import com.firdavs.persianliterature.ui.kit.theme.LocalColors

@Suppress("MagicNumber")
@Composable
fun DrawerSheet(
    chapters: List<Chapter>,
    currentChapter: Chapter,
    onChapterClick: (Chapter) -> Unit,
    appName: String? = null,
    @DrawableRes appIconRes: Int? = null
) {
    val colors = LocalColors.current
    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.7f),
        drawerContainerColor = colors.background
    ) {
        if (appName != null && appIconRes != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = appIconRes),
                    contentDescription = appName,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = appName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = colors.text
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = colors.primary.copy(alpha = 0.3f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chapters) { chapter ->
                val isCurrentChapter = chapter == currentChapter
                H4Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isCurrentChapter) { onChapterClick(chapter) }
                        .background(
                            color = if (isCurrentChapter) colors.primary.copy(alpha = 0.5f) else colors.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    res = chapter.titleRes
                )
            }
        }
    }
}
