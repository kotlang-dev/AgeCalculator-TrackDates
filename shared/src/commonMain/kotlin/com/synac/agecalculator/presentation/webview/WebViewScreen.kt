package com.synac.agecalculator.presentation.webview

import androidx.compose.runtime.Composable

@Composable
expect fun WebViewScreen(
    url: String,
    onNavigateBack: () -> Unit
)