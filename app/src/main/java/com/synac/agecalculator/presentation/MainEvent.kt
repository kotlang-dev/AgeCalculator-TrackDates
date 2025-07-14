package com.synac.agecalculator.presentation

sealed interface MainEvent {
    data class ShowToast(val message: String) : MainEvent
    data object StartUpdateFlow : MainEvent
}