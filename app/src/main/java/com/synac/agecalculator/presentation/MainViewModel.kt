package com.synac.agecalculator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.util.AppTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val preferenceRepository: PreferenceRepository,
    private val appUpdateRepository: AppUpdateRepository
) : ViewModel() {

    private val _event = Channel<MainEvent>()
    val event = _event.receiveAsFlow()

    private val _isUpdateReadyToInstall = MutableStateFlow(false)
    val isUpdateReadyToInstall = _isUpdateReadyToInstall.asStateFlow()

    fun setUpdateReadyToInstall(isReady: Boolean) {
        _isUpdateReadyToInstall.value = isReady
    }

    val themeSetting: Flow<Int> = preferenceRepository
        .getPreference(
            key = PrefsKey.AppTheme,
            defaultValue = AppTheme.AUTO.value
        )

    fun checkForUpdate(isTriggeredManually: Boolean = false) {
        viewModelScope.launch {
            val status = appUpdateRepository.getUpdateStatus()

            if (!status.isUpdateAvailable) {
                if (isTriggeredManually) {
                    _event.send(MainEvent.ShowToast("You are on the latest version."))
                }
                return@launch
            }

            if (status.stalenessDays < MIN_STALENESS_DAYS) {
                if (isTriggeredManually) {
                    _event.send(MainEvent.ShowToast("You are on the latest version."))
                }
                return@launch
            }

            val lastPromptTimeStamp = preferenceRepository.getPreference(
                key = PrefsKey.LastUpdatePrompt,
                defaultValue = 0L
            ).first()
            val timeSinceLastPrompt = System.currentTimeMillis() - lastPromptTimeStamp
            if (timeSinceLastPrompt < PROMPT_COOLDOWN_MILLIS && !isTriggeredManually) {
                return@launch
            }

            _event.send(MainEvent.StartUpdateFlow)
        }
    }

    fun recordUpdatePromptShown() {
        viewModelScope.launch {
            preferenceRepository.savePreference(
                key = PrefsKey.LastUpdatePrompt,
                value = System.currentTimeMillis()
            )
        }
    }

    companion object {
        private const val MIN_STALENESS_DAYS = 2
        private const val PROMPT_COOLDOWN_MILLIS = 7 * 24 * 60 * 60 * 1000L // 7 days
    }
}