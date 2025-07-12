package com.synac.agecalculator.presentation.calculator

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.domain.repository.OccasionRepository
import com.synac.agecalculator.domain.repository.ReminderScheduler
import com.synac.agecalculator.presentation.navigation.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until

class CalculatorViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: OccasionRepository,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    val occasionIdNavArg = savedStateHandle.toRoute<Route.CalculatorScreen>().id

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _event = Channel<CalculatorEvent>()
    val event = _event.receiveAsFlow()

    private var saveJob: Job? = null

    init {
        getOccasion()
    }

    fun onAction(action: CalculatorAction) {
        when (action) {
            CalculatorAction.ShowEmojiPicker -> {
                _uiState.update { it.copy(isEmojiDialogOpen = true) }
            }

            CalculatorAction.DismissEmojiPicker -> {
                _uiState.update { it.copy(isEmojiDialogOpen = false) }
            }

            is CalculatorAction.EmojiSelected -> {
                _uiState.update { it.copy(isEmojiDialogOpen = false, emoji = action.emoji) }
                saveOccasion()
            }

            is CalculatorAction.ShowDatePicker -> {
                _uiState.update {
                    it.copy(
                        isDatePickerDialogOpen = true,
                        activeDateField = action.dateField
                    )
                }
            }

            CalculatorAction.DismissDatePicker -> {
                _uiState.update { it.copy(isDatePickerDialogOpen = false) }
            }

            is CalculatorAction.SetTitle -> {
                _uiState.update { it.copy(title = action.title) }
                saveOccasion()
            }

            is CalculatorAction.DateSelected -> onDateSelected(action.millis)
            CalculatorAction.DeleteOccasion -> deleteOccasion()
            CalculatorAction.ToggleReminder -> toggleReminder()
            CalculatorAction.NavigateUp -> {}
        }
    }

    private fun saveOccasion() {
        saveJob?.cancel()
        viewModelScope.launch {
            delay(1000)
            val occasion = Occasion(
                id = uiState.value.occasionId,
                dateMillis = uiState.value.fromDateMillis,
                emoji = uiState.value.emoji,
                title = uiState.value.title.trim(),
                isReminderEnabled = uiState.value.isReminderEnabled
            )
            val occasionId = repository.insertOccasion(occasion)
            _uiState.update { it.copy(occasionId = occasionId) }
            saveJob = null
        }
    }

    private fun getOccasion() {
        if (occasionIdNavArg == null) return
        viewModelScope.launch {
            repository.getOccasionById(occasionIdNavArg)?.let { occasion ->
                _uiState.update {
                    it.copy(
                        fromDateMillis = occasion.dateMillis,
                        emoji = occasion.emoji,
                        title = occasion.title,
                        occasionId = occasion.id,
                        isReminderEnabled = occasion.isReminderEnabled
                    )
                }
            }
            calculateStats()
        }
    }

    private fun deleteOccasion() {
        val occasionId = uiState.value.occasionId
        if (occasionId == null) return
        viewModelScope.launch {
            repository.deleteOccasion(occasionId)
            reminderScheduler.cancel(occasionId)
            _event.send(CalculatorEvent.ShowToast("Deleted Successfully"))
            _event.send(CalculatorEvent.NavigateToDashboardScreen)
        }
    }

    private fun toggleReminder() {
        _uiState.update { it.copy(isReminderEnabled = !it.isReminderEnabled) }
        saveOccasion()
        val occasion = Occasion(
            id = uiState.value.occasionId,
            dateMillis = uiState.value.fromDateMillis,
            emoji = uiState.value.emoji,
            title = uiState.value.title,
            isReminderEnabled = uiState.value.isReminderEnabled
        )
        if (occasion.isReminderEnabled) {
            reminderScheduler.schedule(occasion)
            _event.trySend(CalculatorEvent.ShowToast("Reminder Enabled"))
        } else {
            occasion.id?.let { reminderScheduler.cancel(it) }
            _event.trySend(CalculatorEvent.ShowToast("Reminder Disabled"))
        }
    }

    private fun onDateSelected(dateMillis: Long?) {
        when (uiState.value.activeDateField) {
            DateField.FROM -> {
                _uiState.update {
                    it.copy(isDatePickerDialogOpen = false, fromDateMillis = dateMillis)
                }
                saveOccasion()
            }

            DateField.TO -> {
                _uiState.update {
                    it.copy(isDatePickerDialogOpen = false, toDateMillis = dateMillis)
                }
            }
        }
        calculateStats()
    }

    private fun calculateStats() {
        val timeZone = TimeZone.currentSystemDefault()
        val fromMillis = _uiState.value.fromDateMillis ?: System.currentTimeMillis()
        val toMillis = _uiState.value.toDateMillis ?: System.currentTimeMillis()

        //TODO update to new date time api
        val fromInstant = Instant.fromEpochMilliseconds(fromMillis)
        val toInstant = Instant.fromEpochMilliseconds(toMillis)

        val period = fromInstant.periodUntil(toInstant, timeZone)
        val diffInMonths = fromInstant.until(toInstant, DateTimeUnit.MONTH, timeZone)
        val diffInWeeks = fromInstant.until(toInstant, DateTimeUnit.WEEK, timeZone)
        val diffInDays = fromInstant.until(toInstant, DateTimeUnit.DAY, timeZone)
        val diffInHours = fromInstant.until(toInstant, DateTimeUnit.HOUR, timeZone)
        val diffInMinutes = fromInstant.until(toInstant, DateTimeUnit.MINUTE, timeZone)
        val diffInSeconds = fromInstant.until(toInstant, DateTimeUnit.SECOND, timeZone)

        //Calculate next Anniversary
        val fromDate = fromInstant.toLocalDateTime(timeZone).date
        val toDate = toInstant.toLocalDateTime(timeZone).date

        var nextAnniversary = LocalDate(toDate.year, fromDate.month, fromDate.dayOfMonth)
        if (nextAnniversary < toDate) {
            nextAnniversary = LocalDate(toDate.year + 1, fromDate.month, fromDate.dayOfMonth)
        }

        val nextAnniversaryInstant = nextAnniversary.atStartOfDayIn(timeZone)
        val upcomingPeriod = toInstant.periodUntil(nextAnniversaryInstant, timeZone)


        _uiState.update {
            it.copy(
                passedPeriod = period,
                upcomingPeriod = upcomingPeriod,
                ageStats = AgeStats(
                    years = period.years,
                    months = diffInMonths.toInt(),
                    weeks = diffInWeeks.toInt(),
                    days = diffInDays.toInt(),
                    hours = diffInHours.toInt(),
                    minutes = diffInMinutes.toInt(),
                    seconds = diffInSeconds.toInt()
                )
            )
        }
    }
}









