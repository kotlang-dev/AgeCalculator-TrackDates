package com.synac.agecalculator.presentation.util

enum class AppTheme {
    LIGHT,
    DARK,
    AUTO;

    companion object {
        fun fromString(name: String?): AppTheme {
            return entries.find { it.name == name } ?: AUTO
        }
    }
}