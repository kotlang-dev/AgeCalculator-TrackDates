package com.synac.agecalculator.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object DashboardScreen : Route
    @Serializable
    data class CalculatorScreen(val id: Int?) : Route
    @Serializable
    data object SettingsScreen : Route
    @Serializable
    data class WebView(val url: String) : Route
}