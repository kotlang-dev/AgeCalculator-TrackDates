package com.synac.agecalculator.presentation.list_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.model.CalculationResult
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.domain.usecase.CalculateStatsUseCase
import com.synac.agecalculator.presentation.calculator.CalculatorUiState
import com.synac.agecalculator.presentation.calculator.DateField
import com.synac.agecalculator.util.AppLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListDetailViewModel(
    private val repository: OccasionRepository,
    private val calculateStatsUseCase: CalculateStatsUseCase
) : ViewModel() {

    private val TAG = "ListDetailViewModel"

    private val _uiState = MutableStateFlow(ListDetailUiState())
    val uiState: StateFlow<ListDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            if (repository.getOccasionCount() == 0) {
                AppLogger.d(TAG, "init: No occasions found, creating default.")
                val default = Occasion(
                    id = null,
                    title = "Birthday",
                    dateMillis = System.currentTimeMillis(),
                    emoji = "🎂"
                )
                repository.insertOccasion(default)
            }

            repository.observeOccasions().collect { occasions ->
                AppLogger.d(TAG, "collect: Received ${occasions.size} occasions.")

                _uiState.update { currentState ->
                    val stateWithNewOccasions = currentState.copy(
                        dashboardState = currentState.dashboardState.copy(occasions = occasions)
                    )

                    if (occasions.isNotEmpty() && !stateWithNewOccasions.isInitialSelectionDone) {
                        AppLogger.d(TAG, "collect: Performing initial selection.")
                        val firstOccasion = occasions.first()
                        val calculationResult = calculateStatsUseCase(
                            fromDateMillis = firstOccasion.dateMillis,
                            toDateMillis = stateWithNewOccasions.calculatorState.toDateMillis
                        )
                        stateWithNewOccasions.copy(
                            selectedOccasion = firstOccasion,
                            calculatorState = firstOccasion.toCalculatorUiState(calculationResult),
                            isInitialSelectionDone = true
                        )
                    } else {
                        stateWithNewOccasions
                    }
                }
            }
        }
    }

    private val _event = Channel<ListDetailEvent>()
    val event = _event.receiveAsFlow()

    private var saveJob: Job? = null

    fun onAction(action: ListDetailAction) {
        when (action) {
            is ListDetailAction.SetTitle -> handleSetTitle(action.title)
            is ListDetailAction.EmojiSelected -> handleSetEmoji(action.emoji)
            is ListDetailAction.DateSelected -> handleDateSelected(action.millis)
            is ListDetailAction.ShowDatePicker -> handleShowDatePicker(action.dateField)
            ListDetailAction.ShowEmojiPicker -> handleShowEmojiPicker()
            ListDetailAction.DeleteOccasion -> deleteOccasion()
            ListDetailAction.DismissDatePicker -> handleDismissDatePicker()
            ListDetailAction.DismissEmojiPicker -> handleDismissEmojiPicker()
            is ListDetailAction.OccasionSelected -> handleOccasionSelected(action.occasionId)
            ListDetailAction.AddNewOccasionClicked -> handleAddNewOccasion()
            ListDetailAction.SaveOccasionChanges -> {}
            ListDetailAction.NavigateUp -> {}
            ListDetailAction.NavigateToSettingsScreen -> {}
        }
    }

    private fun handleSetTitle(title: String) {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(title = title)
            )
        }
        saveOccasion()
    }

    private fun handleSetEmoji(emoji: String) {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(
                    emoji = emoji,
                    isEmojiDialogOpen = false
                )
            )
        }
        saveOccasion()
    }

    private fun handleShowEmojiPicker() {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(isEmojiDialogOpen = true)
            )
        }
    }

    private fun handleShowDatePicker(dateField: DateField) {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(
                    isDatePickerDialogOpen = true,
                    activeDateField = dateField
                )
            )
        }
    }

    private fun handleDismissDatePicker() {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(isDatePickerDialogOpen = false)
            )
        }
    }

    private fun handleDismissEmojiPicker() {
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(isEmojiDialogOpen = false)
            )
        }
    }

    private fun handleOccasionSelected(occasionId: Int?) {
        val selectedOccasion = uiState.value.dashboardState.occasions
            .find { it.id == occasionId } ?: return

        val calculationResult = calculateStatsUseCase(
            fromDateMillis = selectedOccasion.dateMillis,
            toDateMillis = uiState.value.calculatorState.toDateMillis
        )

        _uiState.update {
            it.copy(
                selectedOccasion = selectedOccasion,
                calculatorState = selectedOccasion.toCalculatorUiState(calculationResult)
            )
        }
    }

    private fun handleAddNewOccasion() {
        AppLogger.d(TAG, "Action: handleAddNewOccasion called. Setting selectedOccasion to null.")
        _uiState.update {
            it.copy(
                selectedOccasion = null,
                calculatorState = CalculatorUiState()
            )
        }
    }

    private fun handleDateSelected(millis: Long?) {
        val currentCalculatorState = uiState.value.calculatorState
        val activeField = currentCalculatorState.activeDateField

        val newFromMillis =
            if (activeField == DateField.FROM) millis else currentCalculatorState.fromDateMillis
        val newToMillis =
            if (activeField == DateField.TO) millis else currentCalculatorState.toDateMillis

        val calculationResult = calculateStatsUseCase(newFromMillis, newToMillis)
        _uiState.update {
            it.copy(
                calculatorState = it.calculatorState.copy(
                    isDatePickerDialogOpen = false,
                    fromDateMillis = newFromMillis,
                    toDateMillis = newToMillis,
                    passedPeriod = calculationResult.passedPeriod,
                    upcomingPeriod = calculationResult.upcomingPeriod,
                    ageStats = calculationResult.ageStats
                )
            )
        }
        if (activeField == DateField.FROM) {
            saveOccasion()
        }
    }

    private fun saveOccasion() {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            delay(500) // Debounce save operations
            val currentCalculatorState = uiState.value.calculatorState
            val currentId = uiState.value.selectedOccasion?.id

            val occasionToSave = Occasion(
                id = currentId,
                dateMillis = currentCalculatorState.fromDateMillis,
                emoji = currentCalculatorState.emoji,
                title = currentCalculatorState.title
            )
            val savedId = repository.insertOccasion(occasionToSave)
            if (currentId == null) {
                _uiState.update {
                    it.copy(
                        selectedOccasion = occasionToSave.copy(id = savedId)
                    )
                }
            }
        }
    }

    private fun deleteOccasion() {
        val occasionToDelete = uiState.value.selectedOccasion ?: return
        viewModelScope.launch {
            repository.deleteOccasion(occasionToDelete.id!!)
            _uiState.update { it.copy(selectedOccasion = null) }
            _event.send(ListDetailEvent.ShowSnackbar("Deleted Successfully"))
        }
    }

    private fun Occasion.toCalculatorUiState(result: CalculationResult): CalculatorUiState {
        return CalculatorUiState(
            title = this.title,
            emoji = this.emoji,
            fromDateMillis = this.dateMillis,
            passedPeriod = result.passedPeriod,
            upcomingPeriod = result.upcomingPeriod,
            ageStats = result.ageStats
        )
    }

}

