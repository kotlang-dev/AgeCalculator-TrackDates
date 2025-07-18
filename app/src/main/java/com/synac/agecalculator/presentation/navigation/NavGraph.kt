package com.synac.agecalculator.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.synac.agecalculator.presentation.calculator.CalculatorScreenRoot
import com.synac.agecalculator.presentation.dashboard.DashboardScreenRoot
import com.synac.agecalculator.presentation.settings.SettingsScreenRoot
import com.synac.agecalculator.presentation.util.decodeUrl
import com.synac.agecalculator.presentation.util.encodeUrl
import com.synac.agecalculator.presentation.webview.WebViewScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    onAppVersionClick: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        startDestination = Route.DashboardScreen,
        navController = navController
    ) {
        composable<Route.DashboardScreen> {
            DashboardScreenRoot(
                navigateToCalculatorScreen = { occasionId ->
                    navController.navigate(Route.CalculatorScreen(occasionId))
                },
                navigateToSettingsScreen = {
                    navController.navigate(Route.SettingsScreen)
                }
            )
        }
        composable<Route.CalculatorScreen>(
            enterTransition = { slideInTransition() },
            exitTransition = { slideOutTransition() },
        ) {
            CalculatorScreenRoot(
                navigateUp = navController::navigateUp
            )
        }
        composable<Route.SettingsScreen> {
            SettingsScreenRoot(
                navigateUp = navController::navigateUp,
                navigateToPrivacyPolicy = { policyUrl ->
                    navController.navigate(Route.WebView(policyUrl.encodeUrl()))
                },
                onAppVersionClick = onAppVersionClick
            )
        }
        composable<Route.WebView>(
            enterTransition = { slideInTransition() },
            exitTransition = { slideOutTransition() },
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Route.WebView>()
            WebViewScreen(
                url = args.url.decodeUrl(),
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}

fun slideInTransition() = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(300)
)

fun slideOutTransition() = slideOutHorizontally(
    targetOffsetX = { it },
    animationSpec = tween(300)
)