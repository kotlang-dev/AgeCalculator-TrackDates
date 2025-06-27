package com.synac.agecalculator.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.repository.OccasionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: OccasionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = combine(
        _uiState,
        repository.observeOccasions()
    ) { state, occasions ->
        state.copy(occasions = occasions)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = DashboardUiState()
    )

    fun onAction(action: DashboardAction) {
        when (action) {
            is DashboardAction.ShowDatePicker -> {
                _uiState.update {
                    it.copy(
                        isDatePickerDialogOpen = true,
                        selectedOccasion = action.occasion
                    )
                }
            }

            DashboardAction.DismissDatePicker -> {
                _uiState.update { it.copy(isDatePickerDialogOpen = false) }
            }

            is DashboardAction.DateSelected -> {
                _uiState.update { it.copy(isDatePickerDialogOpen = false) }
                updateOccasion(action.millis)
            }
        }
    }

    private fun updateOccasion(dateMillis: Long?) {
        viewModelScope.launch {
            uiState.value.selectedOccasion?.let {
                val updatedOccasion = it.copy(dateMillis = dateMillis)
                repository.insertOccasion(updatedOccasion)
            }
        }
    }

}