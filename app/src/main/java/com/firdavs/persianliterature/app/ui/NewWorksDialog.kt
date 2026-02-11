package com.firdavs.persianliterature.app.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.firdavs.persianliterature.author_api.model.NewWorkItem
import com.firdavs.persianliterature.ui.kit.H3Text
import com.firdavs.persianliterature.ui.kit.H5BoldText
import com.firdavs.persianliterature.ui.kit.H5Text
import com.firdavs.persianliterature.ui.kit.T2Text
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalAppLocale
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.theme.localizedContext
import com.firdavs.persianliterature.ui.kit.theme.stringResource

@Composable
fun NewWorksDialog(
    newWorks: List<NewWorkItem>,
    onWorkClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // Group works by language
    val englishWorks = newWorks.filter { it.language == "en" }
    val russianWorks = newWorks.filter { it.language == "ru" }
    val tajikWorks = newWorks.filter { it.language == "tj" }
    val backgroundColor = LocalColors.current.background

    // Get current locale to check language matches
    val currentLocale = LocalAppLocale.current
    val currentLanguageCode = when (currentLocale.language) {
        "en" -> "en"
        "ru" -> "ru"
        "tg" -> "tj"
        else -> "en"
    }

    val toastContext = localizedContext()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                H3Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.new_works_available)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // English works section
                    if (englishWorks.isNotEmpty()) {
                        item {
                            H5BoldText(
                                text = stringResource(id = R.string.new_works_in_english)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(englishWorks) { work ->
                            NewWorkItem(
                                work = work,
                                onClick = {
                                    if (work.language == currentLanguageCode) {
                                        onWorkClick(work.id)
                                        onDismiss()
                                    } else {
                                        Toast.makeText(
                                            toastContext,
                                            toastContext.getString(R.string.change_language_to_view_work),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Russian works section
                    if (russianWorks.isNotEmpty()) {
                        item {
                            H5BoldText(
                                text = stringResource(id = R.string.new_works_in_russian)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(russianWorks) { work ->
                            NewWorkItem(
                                work = work,
                                onClick = {
                                    if (work.language == currentLanguageCode) {
                                        onWorkClick(work.id)
                                        onDismiss()
                                    } else {
                                        Toast.makeText(
                                            toastContext,
                                            toastContext.getString(R.string.change_language_to_view_work),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    // Tajik works section
                    if (tajikWorks.isNotEmpty()) {
                        item {
                            H5BoldText(
                                text = stringResource(id = R.string.new_works_in_tajik)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(tajikWorks) { work ->
                            NewWorkItem(
                                work = work,
                                onClick = {
                                    if (work.language == currentLanguageCode) {
                                        onWorkClick(work.id)
                                        onDismiss()
                                    } else {
                                        Toast.makeText(
                                            toastContext,
                                            toastContext.getString(R.string.change_language_to_view_work),
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onDismiss
                        ) {
                            H5Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(id = R.string.close),
                                color = AppTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NewWorkItem(
    work: NewWorkItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = LocalColors.current.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            H5Text(
                text = work.title
            )
            Spacer(modifier = Modifier.height(4.dp))
            T2Text(
                text = work.author
            )
        }
    }
}
