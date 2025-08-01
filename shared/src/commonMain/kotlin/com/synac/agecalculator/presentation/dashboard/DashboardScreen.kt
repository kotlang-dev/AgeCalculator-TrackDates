package com.synac.agecalculator.presentation.dashboard

import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.presentation.calculator.DateField
import com.synac.agecalculator.presentation.component.CustomDatePickerDialog
import com.synac.agecalculator.presentation.component.StylizedAgeText
import com.synac.agecalculator.presentation.list_detail.ListDetailAction
import com.synac.agecalculator.presentation.theme.gradient
import com.synac.agecalculator.presentation.theme.greenTextColor
import com.synac.agecalculator.presentation.theme.spacing
import com.synac.agecalculator.presentation.util.periodUntil
import com.synac.agecalculator.presentation.util.toFormattedDateString
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    selectedOccasionId: Int?,
    onAction: (ListDetailAction) -> Unit
) {

    CustomDatePickerDialog(
        isOpen = state.isDatePickerDialogOpen,
        onDismissRequest = { onAction(ListDetailAction.DismissDatePicker) },
        onConfirmButtonClick = { selectedDateMillis ->
            onAction(ListDetailAction.DateSelected(selectedDateMillis))
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        DashboardTopBar(
            onAddIconClick = { onAction(ListDetailAction.AddNewOccasionClicked) },
            onSettingsIconClick = { onAction(ListDetailAction.NavigateToSettingsScreen) }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
        ) {
            items(state.occasions) { occasion ->
                OccasionCard(
                    modifier = Modifier.fillMaxWidth(),
                    occasion = occasion,
                    isCardSelected = occasion.id == selectedOccasionId,
                    onCalendarIconClick = {
                        onAction(ListDetailAction.ShowDatePicker(DateField.FROM))
                    },
                    onClick = { onAction(ListDetailAction.OccasionSelected(occasion.id)) }
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
    isCardSelected: Boolean,
    onCalendarIconClick: () -> Unit,
    onClick: () -> Unit
) {
    val dateMillis = occasion.dateMillis
    val buttonContainerColor = if (isCardSelected) {
        MaterialTheme.colorScheme.greenTextColor
    } else MaterialTheme.colorScheme.surface
    val buttonContentColor = if (isCardSelected) {
        MaterialTheme.colorScheme.surface
    } else MaterialTheme.colorScheme.onSurface

    ElevatedCard(
        modifier = modifier
            .clickable { onClick() }
            .border(1.dp, gradient, MaterialTheme.shapes.medium)
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
                containerColor = buttonContainerColor,
                contentColor = buttonContentColor
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

@Preview
@Composable
private fun PreviewDashboardScreen() {
    val dummyOccasions = List(20) {
        Occasion(id = 1, title = "Birthday", dateMillis = 0L, emoji = "🎂")
    }
    DashboardScreen(
        state = DashboardUiState(occasions = dummyOccasions),
        selectedOccasionId = 1,
        onAction = {}
    )
}