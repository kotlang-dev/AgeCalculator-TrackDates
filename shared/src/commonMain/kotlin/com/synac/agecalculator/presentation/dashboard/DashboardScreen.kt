package com.synac.agecalculator.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.presentation.component.CustomDatePickerDialog
import com.synac.agecalculator.presentation.component.StylizedAgeText
import com.synac.agecalculator.presentation.theme.spacing
import com.synac.agecalculator.presentation.util.periodUntil
import com.synac.agecalculator.presentation.util.toFormattedDateString
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DashboardScreenRoot(
    navigateToCalculatorScreen: (Int?) -> Unit,
    navigateToSettingsScreen: () -> Unit,
) {
    val viewModel: DashboardViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DashboardScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is DashboardAction.NavigateToCalculatorScreen -> {
                    navigateToCalculatorScreen(action.occasionId)
                }
                is DashboardAction.NavigateToSettingsScreen -> navigateToSettingsScreen()
                else -> viewModel.onAction(action)
            }
        }
    )
}

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onAction: (DashboardAction) -> Unit
) {

    CustomDatePickerDialog(
        isOpen = state.isDatePickerDialogOpen,
        onDismissRequest = { onAction(DashboardAction.DismissDatePicker) },
        onConfirmButtonClick = { selectedDateMillis ->
            onAction(DashboardAction.DateSelected(selectedDateMillis))
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DashboardTopBar(
            onAddIconClick = { onAction(DashboardAction.NavigateToCalculatorScreen(null)) },
            onSettingsIconClick = { onAction(DashboardAction.NavigateToSettingsScreen) }
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state.occasions) { occasion ->
                OccasionCard(
                    modifier = Modifier.fillMaxWidth(),
                    occasion = occasion,
                    onCalendarIconClick = { onAction(DashboardAction.ShowDatePicker(occasion)) },
                    onClick = { onAction(DashboardAction.NavigateToCalculatorScreen(occasion.id)) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(
    modifier: Modifier = Modifier,
    onAddIconClick: () -> Unit,
    onSettingsIconClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = "Dashboard") },
        actions = {
            IconButton(onClick = onAddIconClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
            IconButton(onClick = onSettingsIconClick) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    )
}

@Composable
private fun OccasionCard(
    modifier: Modifier = Modifier,
    occasion: Occasion,
    onCalendarIconClick: () -> Unit,
    onClick: () -> Unit
) {
    val dateMillis = occasion.dateMillis
    Card(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.small,
                    top = MaterialTheme.spacing.medium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = occasion.emoji, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Column {
                Text(text = occasion.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = dateMillis.toFormattedDateString(), color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onCalendarIconClick) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Add"
                )
            }
        }
        StylizedAgeText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = MaterialTheme.spacing.small,
                    top = MaterialTheme.spacing.medium
                ),
            years = dateMillis.periodUntil().years,
            months = dateMillis.periodUntil().months,
            days = dateMillis.periodUntil().days
        )
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
                .align(Alignment.End)
                .size(25.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Icon(
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Show Details"
            )
        }
    }
}

//@PreviewScreenSizes
@Composable
private fun PreviewDashboardScreen() {
    val dummyOccasions = List(20) {
        Occasion(id = 1, title = "Birthday", dateMillis = 0L, emoji = "🎂")
    }
    DashboardScreen(
        state = DashboardUiState(occasions = dummyOccasions),
        onAction = {}
    )
}