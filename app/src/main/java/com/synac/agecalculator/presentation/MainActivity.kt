package com.synac.agecalculator.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.presentation.navigation.NavGraph
import com.synac.agecalculator.presentation.theme.AgeCalculatorTheme
import com.synac.agecalculator.presentation.util.AppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {

            val mainViewModel = koinViewModel<MainViewModel>()
            val appTheme by mainViewModel.themeSetting
                .collectAsStateWithLifecycle(initialValue = AppTheme.AUTO.value)
            val isDarkMode = when(appTheme) {
                AppTheme.LIGHT.value -> false
                AppTheme.DARK.value -> true
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
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .consumeWindowInsets(WindowInsets.systemBars)
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}