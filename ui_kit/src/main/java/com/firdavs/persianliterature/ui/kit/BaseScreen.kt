package com.firdavs.persianliterature.ui.kit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.unit.dp
import com.firdavs.persianliterature.ui.kit.theme.AppTheme
import com.firdavs.persianliterature.ui.kit.theme.LocalColors
import com.firdavs.persianliterature.ui.kit.util.thenIfNotNull
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    drawerContent: (@Composable () -> Unit)? = null,
    topBar: @Composable (DrawerState, CoroutineScope) -> Unit,
    mainContent: @Composable () -> Unit,
    footerContent: (@Composable BoxScope.() -> Unit)? = null
) {
    val backgroundColor = LocalColors.current.background
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val content = @Composable {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = backgroundColor,
            topBar = { topBar(drawerState, scope) },
            content = { innerPadding ->
                Column(Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(innerPadding)
                            .thenIfNotNull(footerContent) { drawShadow() }
                    ) {
                        mainContent()
                    }

                    if (footerContent != null) {
                        Box(
                            modifier = Modifier.Companion
                                .background(AppTheme.colors.onPrimary)
                                .fillMaxWidth()
                                .padding(16.dp),
                            content = footerContent
                        )
                    }
                }
            }
        )
    }

    if (drawerContent != null) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = drawerContent,
            content = content
        )
    } else {
        content()
    }
}


private fun Modifier.drawShadow() = drawBehind {
    val shadowHeight = 7.dp.toPx()
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Black.copy(alpha = 0.02f), Color.Transparent),
            startY = size.height,
            endY = size.height - shadowHeight
        ),
        topLeft = Offset(0f, size.height - shadowHeight),
        size = Size(size.width, shadowHeight)
    )
}
