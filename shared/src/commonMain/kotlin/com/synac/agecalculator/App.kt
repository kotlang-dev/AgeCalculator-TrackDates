package com.synac.agecalculator

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.presentation.main.MainAction
import com.synac.agecalculator.presentation.main.MainEvent
import com.synac.agecalculator.presentation.main.MainViewModel
import com.synac.agecalculator.presentation.navigation.NavGraph
import com.synac.agecalculator.presentation.theme.AgeCalculatorTheme
import com.synac.agecalculator.presentation.util.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App(
    startUpdateFlow: () -> Unit = {},
    completeUpdate: () -> Unit = {},
    enableEdgeToEdge: (Boolean) -> Unit = {}
) {
    val mainViewModel: MainViewModel = koinInject()
    val state by mainViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val isDarkMode = when (state.appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> isSystemInDarkTheme()
    }

    LaunchedEffect(Unit) {
        mainViewModel.event.collect { event ->
            when(event) {
                is MainEvent.ShowToast -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
                MainEvent.StartUpdateFlow -> startUpdateFlow()
            }
        }
    }

    LaunchedEffect(state.isUpdateReadyToInstall) {
        if (state.isUpdateReadyToInstall) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "An update has just been downloaded.",
                    actionLabel = "RESTART",
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    completeUpdate()
                }
            }
        }
    }

    LaunchedEffect(isDarkMode) {
        enableEdgeToEdge(isDarkMode)
    }

    AgeCalculatorTheme(
        darkTheme = isDarkMode
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            if (state.isUpdateDownloading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            NavGraph(
                modifier = Modifier
                    .consumeWindowInsets(WindowInsets.systemBars)
                    .padding(innerPadding),
                snackbarHostState = snackbarHostState,
                onAppVersionClick = {
                    if (!state.isUpdateDownloading) {
                        mainViewModel.onAction(MainAction.CheckForUpdate(manually = true))
                    }
                }
            )
        }
    }
}