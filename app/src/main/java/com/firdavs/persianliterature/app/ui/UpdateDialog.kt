package com.firdavs.persianliterature.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.firdavs.persianliterature.app.R
import com.firdavs.persianliterature.settings.api.ReleaseNotes
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5BoldText
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.stringResource

@Composable
fun UpdateDialog(
    releaseNotes: List<ReleaseNotes>,
    onUpdate: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = AppTheme.colors.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                H3Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.update_available_title),
                    color = AppTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (releaseNotes.isEmpty()) {
                    H5Text(
                        text = stringResource(id = R.string.update_available_message),
                        color = AppTheme.colors.onSurface
                    )
                } else {
                    ReleaseNotesContent(releaseNotes)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    TextButton(onClick = onDismiss) {
                        H5Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.update_later_button),
                            color = AppTheme.colors.primary
                        )
                    }
                    TextButton(onClick = onUpdate) {
                        H5Text(
                            textAlign = TextAlign.Center,
                            text = stringResource(id = R.string.update_button),
                            color = AppTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReleaseNotesContent(releaseNotes: List<ReleaseNotes>) {
    // Show version headers only when the user skipped several versions
    val showVersionNames = releaseNotes.size > 1

    H5BoldText(
        text = stringResource(id = R.string.update_whats_new_title),
        color = AppTheme.colors.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 320.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        releaseNotes.forEach { notes ->
            if (showVersionNames && notes.versionName.isNotBlank()) {
                H5BoldText(
                    modifier = Modifier.padding(top = 4.dp),
                    text = notes.versionName,
                    color = AppTheme.colors.onSurface
                )
            }
            notes.items.forEach { item ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    H5Text(
                        text = "•",
                        color = AppTheme.colors.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    H5Text(
                        text = item,
                        color = AppTheme.colors.onSurface
                    )
                }
            }
        }
    }
}
