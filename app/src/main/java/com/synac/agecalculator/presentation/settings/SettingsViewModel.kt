package com.synac.agecalculator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.util.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    appUpdateRepository: AppUpdateRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SettingsUiState(appVersion = appUpdateRepository.getAppVersion())
    )
    val uiState = combine(
        _uiState,
        preferenceRepository.getPreference(PrefsKey.AppTheme, AppTheme.AUTO.value)
    ) { uiState, themeSetting ->
        uiState.copy(
            appTheme = themeSetting
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onThemeChanged(newThemeValue: Int) {
        viewModelScope.launch {
            preferenceRepository.savePreference(
                key = PrefsKey.AppTheme,
                value = newThemeValue
            )
        }
    }

}