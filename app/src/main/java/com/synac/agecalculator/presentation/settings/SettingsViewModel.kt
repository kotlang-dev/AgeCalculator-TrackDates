package com.synac.agecalculator.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.util.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val themeSetting: Flow<Int> = preferenceRepository
        .getPreference(
            key = PrefsKey.AppTheme,
            defaultValue = AppTheme.AUTO.value
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