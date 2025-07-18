package com.synac.agecalculator.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.util.AppTheme
import com.synac.agecalculator.presentation.util.Constants.MIN_STALENESS_DAYS
import com.synac.agecalculator.presentation.util.Constants.PROMPT_COOLDOWN_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val appUpdateRepository: AppUpdateRepository
) : ViewModel() {

    private val _event = Channel<MainEvent>()
    val event = _event.receiveAsFlow()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = combine(
        _uiState,
        preferenceRepository.getPreference(PrefsKey.AppTheme, AppTheme.AUTO.name)
    ) { uiState, themeName ->
        uiState.copy(
            appTheme = AppTheme.fromString(themeName)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun onAction(action: MainAction) {
        when(action) {
            MainAction.AppUpdateDownloaded -> setUpdateDownloaded()
            MainAction.AppUpdateDownloading -> setUpdateDownloading()
            MainAction.AppUpdateUnsuccessful -> setUpdateUnsuccessful()
            is MainAction.CheckForUpdate -> checkForUpdate(action.manually)
            MainAction.RecordUpdateTimestamp -> recordUpdatePromptShown()
        }
    }

    private fun setUpdateDownloaded() {
        _uiState.update {
            it.copy(
                isUpdateDownloading = false,
                isUpdateReadyToInstall = true
            )
        }
    }

    private fun setUpdateDownloading() {
        _uiState.update { it.copy(isUpdateDownloading = true) }
    }

    private fun setUpdateUnsuccessful() {
        _uiState.update { it.copy(isUpdateDownloading = false) }
    }

    private fun checkForUpdate(isTriggeredManually: Boolean) {
        viewModelScope.launch {
            val status = appUpdateRepository.getUpdateStatus()

            if (!status.isUpdateAvailable) {
                if (isTriggeredManually) {
                    _event.send(MainEvent.ShowToast("You are on the latest version."))
                }
                return@launch
            }

            if (!isTriggeredManually) {
                if (status.stalenessDays < MIN_STALENESS_DAYS) {
                    return@launch
                }

                val lastPromptTimeStamp = preferenceRepository.getPreference(
                    key = PrefsKey.LastUpdatePrompt,
                    defaultValue = 0L
                ).first()
                val timeSinceLastPrompt = System.currentTimeMillis() - lastPromptTimeStamp
                if (timeSinceLastPrompt < PROMPT_COOLDOWN_MILLIS) {
                    return@launch
                }
            }

            _event.send(MainEvent.StartUpdateFlow)
        }
    }

    private fun recordUpdatePromptShown() {
        viewModelScope.launch {
            preferenceRepository.savePreference(
                key = PrefsKey.LastUpdatePrompt,
                value = System.currentTimeMillis()
            )
        }
    }
}