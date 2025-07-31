package com.synac.agecalculator.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.synac.agecalculator.presentation.list_detail.ListDetailScreenRoot
import com.synac.agecalculator.presentation.settings.SettingsScreenRoot
import com.synac.agecalculator.presentation.util.decodeUrl
import com.synac.agecalculator.presentation.util.encodeUrl
import com.synac.agecalculator.presentation.webview.WebViewScreen

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    isDarkMode: Boolean,
    onAppVersionClick: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(
        modifier = modifier,
        startDestination = Route.ListDetailScreen,
        navController = navController
    ) {
        composable<Route.ListDetailScreen> {
            ListDetailScreenRoot(
                snackbarHostState = snackbarHostState,
                navigateToSettingsScreen = {
                    navController.navigate(Route.SettingsScreen)
                }
            )
        }
        composable<Route.SettingsScreen> {
            SettingsScreenRoot(
                navigateUp = navController::navigateUp,
                navigateToPrivacyPolicy = { policyUrl ->
                    navController.navigate(Route.WebView(policyUrl.encodeUrl()))
                },
                navigateToAbout = { aboutUrl ->
                    navController.navigate(Route.WebView(aboutUrl.encodeUrl()))
                },
                onAppVersionClick = onAppVersionClick
            )
        }
        composable<Route.WebView>(
            enterTransition = { SlideInHorizontallyTransition },
            exitTransition = { SlideOutHorizontallyTransition },
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<Route.WebView>()
            WebViewScreen(
                url = args.url.decodeUrl(),
                inDarkMode = isDarkMode,
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

val SlideInHorizontallyTransition: EnterTransition = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing,
    ),
) + fadeIn()

val SlideOutHorizontallyTransition: ExitTransition = fadeOut() +
        slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(
                durationMillis = 300,
                easing = LinearEasing,
            ),
        )

fun slideUpTransition() = slideInVertically(
    initialOffsetY = { it },
    animationSpec = tween(300)
)

fun slideDownTransition() = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(300)
)