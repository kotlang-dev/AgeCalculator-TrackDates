package com.synac.agecalculator.presentation.calculator

sealed interface CalculatorAction {
    data object ShowEmojiPicker : CalculatorAction
    data object DismissEmojiPicker : CalculatorAction
    data class EmojiSelected(val emoji: String) : CalculatorAction
    data class ShowDatePicker(val dateField: DateField) : CalculatorAction
    data object DismissDatePicker : CalculatorAction
    data class DateSelected(val millis: Long?) : CalculatorAction
    data class SetTitle(val title: String) : CalculatorAction
    data object DeleteOccasion : CalculatorAction
    data object NavigateUp : CalculatorAction
}