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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.presentation.calculator.CalculatorScreen
import com.synac.agecalculator.presentation.dashboard.DashboardScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ListDetailScreenRoot(
    snackbarHostState: SnackbarHostState,
    navigateToSettingsScreen: () -> Unit,
) {
    val viewModel: ListDetailViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val paneNavigator = rememberListDetailPaneScaffoldNavigator<Any>()

    val isOnlyDetailPaneVisible = paneNavigator.canNavigateBack()

    BackHandler(enabled = isOnlyDetailPaneVisible) {
        scope.launch {
            paneNavigator.navigateBack()
        }
    }

    LaunchedEffect(viewModel.event) {
        viewModel.event.collect { event ->
            when (event) {
                is ListDetailEvent.OccasionDeleted -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = "Deleted Successfully",
                            duration = SnackbarDuration.Short
                        )
                    }
                    if (event.wasSinglePane) {
                        scope.launch {
                            paneNavigator.navigateBack()
                        }
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
                selectedOccasionId = state.selectedOccasion?.id,
                onAction = { action ->
                    when (action) {
                        is ListDetailAction.OccasionSelected -> {
                            viewModel.onAction(ListDetailAction.OccasionSelected(action.occasionId))
                            scope.launch {
                                paneNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                            }
                        }
                        is ListDetailAction.AddNewOccasionClicked -> {
                            viewModel.onAction(ListDetailAction.AddNewOccasionClicked)
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
                isOnlyDetailPaneVisible = isOnlyDetailPaneVisible,
                isDeleteIconVisible = state.selectedOccasion != null,
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