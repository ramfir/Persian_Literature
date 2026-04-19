package com.firdavs.persianliterature.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.firdavs.persianliterature.settings.api.Language
import com.firdavs.persianliterature.ui.kit.theme.AppTheme

@Composable
fun LanguageSelectionDialog(
    onLanguageSelected: (Language) -> Unit
) {
    Dialog(onDismissRequest = { /* Prevent dismissing without selection */ }) {
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
                // Title in all three languages
                Text(
                    text = "Choose Language / Выберите язык / Забонро интихоб кунед",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // English button
                TextButton(
                    onClick = { onLanguageSelected(Language.ENGLISH) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "English",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Russian button
                TextButton(
                    onClick = { onLanguageSelected(Language.RUSSIAN) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Русский",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tajik button
                TextButton(
                    onClick = { onLanguageSelected(Language.TAJIK) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Тоҷикӣ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = AppTheme.colors.primary
                    )
                }
            }
        }
    }
}
