package com.synac.agecalculator.presentation

import androidx.lifecycle.ViewModel
import com.synac.agecalculator.domain.model.PrefsKey
import com.synac.agecalculator.domain.repository.PreferenceRepository
import com.synac.agecalculator.presentation.util.AppTheme
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    preferenceRepository: PreferenceRepository
) : ViewModel() {

    val themeSetting: Flow<Int> = preferenceRepository
        .getPreference(
            key = PrefsKey.AppTheme,
            defaultValue = AppTheme.AUTO.value
        )

}