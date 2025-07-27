package com.synac.agecalculator.presentation.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.synac.agecalculator.App
import com.synac.agecalculator.presentation.util.Constants.APP_UPDATE_REQUEST_CODE
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val appUpdateManager: AppUpdateManager by inject()
    private val mainViewModel: MainViewModel by inject()

    private val installStateUpdateListener = InstallStateUpdatedListener { state ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                mainViewModel.onAction(MainAction.AppUpdateDownloading)
            }

            InstallStatus.DOWNLOADED -> {
                mainViewModel.onAction(MainAction.AppUpdateDownloaded)
            }

            InstallStatus.FAILED,
            InstallStatus.CANCELED -> {
                mainViewModel.onAction(MainAction.AppUpdateUnsuccessful)
            }

            else -> {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        appUpdateManager.registerListener(installStateUpdateListener)

        setContent {
            App(
                startUpdateFlow = { startUpdateFlow() },
                completeUpdate = { appUpdateManager.completeUpdate() },
                enableEdgeToEdge = { isDarkMode ->
                    enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT,
                            detectDarkMode = { isDarkMode }
                        ),
                        navigationBarStyle = SystemBarStyle.auto(
                            Color.TRANSPARENT, Color.TRANSPARENT,
                            detectDarkMode = { isDarkMode }
                        )
                    )
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onAction(MainAction.CheckForUpdate())
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(installStateUpdateListener)
    }

    private fun startUpdateFlow() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            val isUpdateAvailable =
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isFlexibleUpdateAllowed = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            if (isUpdateAvailable && isFlexibleUpdateAllowed) {
                mainViewModel.onAction(MainAction.RecordUpdateTimestamp)

                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                    APP_UPDATE_REQUEST_CODE
                )
            }
        }
    }
}