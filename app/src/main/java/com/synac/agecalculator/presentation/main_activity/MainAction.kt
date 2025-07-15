package com.synac.agecalculator.presentation.main_activity

sealed interface MainAction {
    data object AppUpdateDownloading : MainAction
    data object AppUpdateDownloaded : MainAction
    data object AppUpdateUnsuccessful : MainAction
    data class CheckForUpdate(val manually: Boolean = false) : MainAction
    data object RecordUpdateTimestamp : MainAction
}