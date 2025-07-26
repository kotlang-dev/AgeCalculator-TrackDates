package com.synac.agecalculator.data.repository

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.model.AppUpdateType
import com.synac.agecalculator.domain.model.AppUpdateStatus
import com.synac.agecalculator.domain.repository.AppUpdateRepository
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.google.android.play.core.install.model.UpdateAvailability as AppUpdateAvailability

class AppUpdateRepositoryImpl(
    private val context: Context,
    private val appUpdateManager: AppUpdateManager
) : AppUpdateRepository {

    override fun getAppVersion(): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName ?: ""
        } catch (_: Exception) {
            ""
        }
    }

    override suspend fun getUpdateStatus(): AppUpdateStatus {
        val appUpdateInfo = getAppUpdateInfo()
        return if (appUpdateInfo != null && isFlexibleUpdateAllowed(appUpdateInfo)) {
            AppUpdateStatus(
                isUpdateAvailable = true,
                stalenessDays = appUpdateInfo.clientVersionStalenessDays() ?: -1
            )
        } else {
            AppUpdateStatus(
                isUpdateAvailable = false,
                stalenessDays = -1
            )
        }
    }

    private suspend fun getAppUpdateInfo(): AppUpdateInfo? {
        return suspendCoroutine { continuation ->
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { info -> continuation.resume(info) }
                .addOnFailureListener { continuation.resume(null) }
        }
    }

    private fun isFlexibleUpdateAllowed(appUpdateInfo: AppUpdateInfo): Boolean {
        return appUpdateInfo.updateAvailability() == AppUpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
    }

}