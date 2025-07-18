package com.synac.agecalculator.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.synac.agecalculator.presentation.navigation.NavGraph
import com.synac.agecalculator.presentation.theme.AgeCalculatorTheme
import com.synac.agecalculator.presentation.util.AppTheme
import com.synac.agecalculator.presentation.util.Constants.APP_UPDATE_REQUEST_CODE
import com.synac.agecalculator.presentation.util.showToast
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val appUpdateManager: AppUpdateManager by inject()
    private val mainViewModel: MainViewModel by inject()

    private val installStateUpdateListener = InstallStateUpdatedListener { state ->
        when(state.installStatus()) {
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
            val scope = rememberCoroutineScope()
            val snackbarHostState = remember { SnackbarHostState() }

            val state by mainViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(state.isUpdateReadyToInstall) {
                if (state.isUpdateReadyToInstall) {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "An update has just been downloaded.",
                            actionLabel = "RESTART",
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            appUpdateManager.completeUpdate()
                        }
                    }
                }
            }

            val context = LocalContext.current
            LaunchedEffect(Unit) {
                mainViewModel.event.collect { event ->
                    when(event) {
                        is MainEvent.ShowToast -> showToast(context, event.message)
                        MainEvent.StartUpdateFlow -> startUpdateFlow()
                    }
                }
            }

            val isDarkMode = when (state.appTheme) {
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
                else -> isSystemInDarkTheme()
            }

            LaunchedEffect(isDarkMode) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkMode }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = Color.Transparent.toArgb(),
                        darkScrim = Color.Transparent.toArgb(),
                        detectDarkMode = { isDarkMode }
                    )
                )
            }

            AgeCalculatorTheme(
                darkTheme = isDarkMode
            ) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    if (state.isUpdateDownloading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    NavGraph(
                        modifier = Modifier
                            .consumeWindowInsets(WindowInsets.systemBars)
                            .padding(innerPadding),
                        onAppVersionClick = {
                            if (!state.isUpdateDownloading) {
                                mainViewModel.onAction(MainAction.CheckForUpdate(manually = true))
                            }
                        }
                    )
                }
            }
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