package com.synac.agecalculator.presentation.calculator

sealed interface CalculatorEvent {
    data class ShowToast(val message: String) : CalculatorEvent
    data object NavigateToDashboardScreen : CalculatorEvent
}