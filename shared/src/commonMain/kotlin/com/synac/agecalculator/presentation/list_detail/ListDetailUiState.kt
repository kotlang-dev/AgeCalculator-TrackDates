package com.synac.agecalculator.presentation.list_detail

import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.presentation.calculator.CalculatorUiState
import com.synac.agecalculator.presentation.dashboard.DashboardUiState

data class ListDetailUiState(
    val dashboardState: DashboardUiState = DashboardUiState(),
    val calculatorState: CalculatorUiState = CalculatorUiState(),
    val selectedOccasion: Occasion? = null,
    val isInitialSelectionDone: Boolean = false
)