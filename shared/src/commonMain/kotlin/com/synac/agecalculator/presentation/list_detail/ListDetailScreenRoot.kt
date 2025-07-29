package com.synac.agecalculator.presentation.list_detail

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.presentation.calculator.CalculatorScreen
import com.synac.agecalculator.presentation.dashboard.DashboardScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ListDetailScreenRoot(
    snackbarHostState: SnackbarHostState,
    navigateToSettingsScreen: () -> Unit,
) {
    val viewModel: ListDetailViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val paneNavigator = rememberListDetailPaneScaffoldNavigator<Any>()

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is ListDetailEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    ListDetailPaneScaffold(
        directive = paneNavigator.scaffoldDirective,
        value = paneNavigator.scaffoldValue,
        listPane = {
            DashboardScreen(
                state = state.dashboardState,
                onAction = { action ->
                    when (action) {
                        is ListDetailAction.OccasionSelected -> {
                            viewModel.onAction(ListDetailAction.OccasionSelected(action.occasionId ?: 1))
                            scope.launch {
                                paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                            }
                        }
                        is ListDetailAction.NavigateToSettingsScreen -> navigateToSettingsScreen()
                        else -> viewModel.onAction(action)
                    }
                }
            )
        },
        detailPane = {
            CalculatorScreen(
                state = state.calculatorState,
                onAction = { action ->
                    when (action) {
                        is ListDetailAction.NavigateUp -> {
                            scope.launch {
                                paneNavigator.navigateBack()
                            }
                        }
                        else -> viewModel.onAction(action)
                    }
                }
            )
        }
    )
}