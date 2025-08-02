package com.synac.agecalculator.presentation.settings

import com.synac.agecalculator.presentation.util.AppTheme

sealed interface SettingAction {
    data class ChangeAppTheme(val theme: AppTheme) : SettingAction
    data object PrivacyPolicyClick : SettingAction
    data object AboutClick : SettingAction
    data object OnBackClick : SettingAction
    data object AppVersionClick : SettingAction
}