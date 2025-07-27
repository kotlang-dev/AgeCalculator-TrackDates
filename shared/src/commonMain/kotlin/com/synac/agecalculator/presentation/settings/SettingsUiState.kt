package com.synac.agecalculator.presentation.settings

import com.synac.agecalculator.presentation.util.AppTheme

data class SettingsUiState(
    val appTheme: AppTheme = AppTheme.AUTO,
    val appVersion: String = "",
)