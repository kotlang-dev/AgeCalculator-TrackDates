package com.synac.agecalculator.presentation.dashboard

import com.synac.agecalculator.domain.model.Occasion

sealed interface DashboardAction {
    data object DismissDatePicker : DashboardAction
    data class ShowDatePicker(val occasion: Occasion) : DashboardAction
    data class DateSelected(val millis: Long?) : DashboardAction
    data class NavigateToCalculatorScreen(val occasionId: Int?) : DashboardAction
    data object NavigateToSettingsScreen : DashboardAction
}