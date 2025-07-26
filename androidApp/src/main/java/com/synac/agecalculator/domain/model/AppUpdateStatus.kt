package com.synac.agecalculator.domain.model

data class AppUpdateStatus(
    val isUpdateAvailable: Boolean,
    val stalenessDays: Int
)