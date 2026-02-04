package com.firdavs.persianliterature.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.firdavs.persianliterature.app.R
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.stringResource

@Composable
fun UpdateInstallPrompt(
    onInstall: () -> Unit,
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
                    text = stringResource(id = R.string.update_ready_title),
                    color = AppTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                H5Text(
                    text = stringResource(id = R.string.update_ready_message),
                    color = AppTheme.colors.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(onClick = onDismiss) {
                        H5Text(
                            text = stringResource(id = R.string.update_later_button),
                            color = AppTheme.colors.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = onInstall) {
                        H5Text(
                            text = stringResource(id = R.string.update_install_button),
                            color = AppTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}
