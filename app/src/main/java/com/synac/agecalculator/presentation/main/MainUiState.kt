package com.synac.agecalculator.presentation.main

import com.synac.agecalculator.presentation.util.AppTheme

data class MainUiState(
    val isUpdateDownloading: Boolean = false,
    val isUpdateReadyToInstall: Boolean = false,
    val appTheme: AppTheme = AppTheme.AUTO,
)
