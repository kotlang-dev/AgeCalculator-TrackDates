package com.synac.agecalculator.domain.repository

import com.synac.agecalculator.domain.model.AppUpdateStatus

interface AppUpdateRepository {
    fun getAppVersion(): String
    suspend fun getUpdateStatus(): AppUpdateStatus
}