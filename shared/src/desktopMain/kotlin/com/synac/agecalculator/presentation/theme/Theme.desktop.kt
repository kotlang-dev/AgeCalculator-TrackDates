package com.synac.agecalculator.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
actual fun AgeCalculatorTheme(
    darkTheme: Boolean,
    lightThemeColors: ColorScheme,
    darkThemeColors: ColorScheme,
    typography: Typography,
    content: @Composable (() -> Unit)
) {
    CompositionLocalProvider(
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) darkThemeColors else lightThemeColors,
            typography = typography,
            content = content
        )
    }
}