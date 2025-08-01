package com.synac.agecalculator.presentation.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.awt.Desktop
import java.net.URI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun WebViewScreen(
    url: String,
    inDarkMode: Boolean,
    onNavigateBack: () -> Unit
) {

    LaunchedEffect(url) {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(URI.create(url))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onNavigateBack()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
         Text("Redirecting to browser...")
    }
}
