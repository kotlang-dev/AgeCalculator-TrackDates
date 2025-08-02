package com.synac.agecalculator.presentation.calculator

import com.synac.agecalculator.domain.model.AgeStats
import kotlinx.datetime.DateTimePeriod

data class CalculatorUiState(
    val emoji: String = "🎂",
    val title: String = "",
    val fromDateMillis: Long? = null,
    val toDateMillis: Long? = null,
    val isEmojiBottomSheetOpen: Boolean = false,
    val isDatePickerDialogOpen: Boolean = false,
    val activeDateField: DateField = DateField.FROM,
    val passedPeriod: DateTimePeriod = DateTimePeriod(),
    val upcomingPeriod: DateTimePeriod = DateTimePeriod(),
    val ageStats: AgeStats = AgeStats(),
    val occasionId: Int? = null
)

enum class DateField {
    FROM,
    TO
}

