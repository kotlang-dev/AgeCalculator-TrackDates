package com.synac.agecalculator.presentation.calculator

import kotlinx.datetime.DateTimePeriod

data class CalculatorUiState(
    val emoji: String = "🎂",
    val title: String = "Birthday",
    val fromDateMillis: Long? = null,
    val toDateMillis: Long? = null,
    val isEmojiDialogOpen: Boolean = false,
    val isDatePickerDialogOpen: Boolean = false,
    val activeDateField: DateField = DateField.FROM,
    val period: DateTimePeriod = DateTimePeriod(),
    val ageStats: AgeStats = AgeStats(),
    val occasionId: Int? = null,
    val isReminderEnabled: Boolean = false
)

enum class DateField {
    FROM,
    TO
}

data class AgeStats(
    val years: Int = 0,
    val months: Int = 0,
    val weeks: Int = 0,
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0
)