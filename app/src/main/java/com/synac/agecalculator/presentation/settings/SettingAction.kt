package com.synac.agecalculator.presentation.settings

sealed interface SettingAction {
    data class ChangeAppTheme(val theme: Int) : SettingAction
    data object PrivacyPolicyClick : SettingAction
    data object OnBackClick : SettingAction
    data object AppVersionClick : SettingAction
}