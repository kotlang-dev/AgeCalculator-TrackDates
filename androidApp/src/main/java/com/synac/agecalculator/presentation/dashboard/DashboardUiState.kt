package com.synac.agecalculator.presentation.dashboard

import com.synac.agecalculator.domain.model.Occasion

data class DashboardUiState(
    val isDatePickerDialogOpen: Boolean = false,
    val occasions: List<Occasion> = emptyList(),
    val selectedOccasion: Occasion? = null
)
