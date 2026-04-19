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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.firdavs.persianliterature.app.R
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.stringResource

@Composable
fun NotificationPermissionDialog(
    onAllowClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Dialog(onDismissRequest = onSkipClicked) {
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = stringResource(id = R.string.notification_permission_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Message
                Text(
                    text = stringResource(id = R.string.notification_permission_message),
                    fontSize = 16.sp,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Start,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Skip button
                    TextButton(
                        onClick = onSkipClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.notification_permission_skip),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = AppTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Allow button
                    TextButton(
                        onClick = onAllowClicked,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(id = R.string.notification_permission_allow),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppTheme.colors.primary
                        )
                    }
                }
            }
        }
    }
}
