package com.synac.agecalculator.data.repository

import com.synac.agecalculator.domain.model.AppUpdateStatus
import com.synac.agecalculator.domain.repository.AppUpdateRepository

class DesktopAppUpdateRepository : AppUpdateRepository {

    override fun getAppVersion(): String {
        return System.getProperty("jpackage.app-version", "N/A")
    }

    override suspend fun getUpdateStatus(): AppUpdateStatus {
        return AppUpdateStatus(
            isUpdateAvailable = false,
            stalenessDays = -1
        )
    }
}
