package com.synac.agecalculator.presentation.calculator

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.synac.agecalculator.presentation.component.AgeBoxSection
import com.synac.agecalculator.presentation.component.CustomDatePickerDialog
import com.synac.agecalculator.presentation.component.EmojiPickerDialog
import com.synac.agecalculator.presentation.component.ReminderIcon
import com.synac.agecalculator.presentation.component.StatisticsCard
import com.synac.agecalculator.presentation.theme.AgeCalculatorTheme
import com.synac.agecalculator.presentation.theme.gradient
import com.synac.agecalculator.presentation.theme.spacing
import com.synac.agecalculator.presentation.util.toFormattedDateString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun CalculatorScreen(
    state: CalculatorUiState,
    event: Flow<CalculatorEvent>,
    onAction: (CalculatorAction) -> Unit,
    navigateUp: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {
                is CalculatorEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                CalculatorEvent.NavigateToDashboardScreen -> {
                    navigateUp()
                }
            }
        }
    }

    EmojiPickerDialog(
        isOpen = state.isEmojiDialogOpen,
        onDismissRequest = { onAction(CalculatorAction.DismissEmojiPicker) },
        onEmojiSelected = { selectedEmoji ->
            onAction(CalculatorAction.EmojiSelected(selectedEmoji))
        }
    )

    CustomDatePickerDialog(
        isOpen = state.isDatePickerDialogOpen,
        onDismissRequest = { onAction(CalculatorAction.DismissDatePicker) },
        onConfirmButtonClick = { selectedDateMillis ->
            onAction(CalculatorAction.DateSelected(selectedDateMillis))
        },
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalculatorTopBar(
            isDeleteIconVisible = state.occasionId != null,
            isReminderEnabled = state.isReminderEnabled,
            onBackClick = navigateUp,
            onDeleteClick = { onAction(CalculatorAction.DeleteOccasion) },
            onReminderClick = { onAction(CalculatorAction.ToggleReminder) }
        )
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(MaterialTheme.spacing.medium),
                state = state,
                onAction = onAction
            )
            StatisticsSection(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(MaterialTheme.spacing.medium),
                state = state
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalculatorTopBar(
    modifier: Modifier = Modifier,
    isDeleteIconVisible: Boolean,
    isReminderEnabled: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReminderClick: () -> Unit
) {
    TopAppBar(
        windowInsets = WindowInsets(0),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Navigate Back"
                )
            }
        },
        title = { },
        actions = {
            IconButton(onClick = onReminderClick) {
                ReminderIcon(
                    isReminderEnabled = isReminderEnabled
                )
            }
            if (isDeleteIconVisible) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    )
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    state: CalculatorUiState,
    onAction: (CalculatorAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
                    .border(1.dp, gradient, CircleShape)
                    .clickable { onAction(CalculatorAction.ShowEmojiPicker) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.emoji,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, gradient, MaterialTheme.shapes.medium),
                value = state.title,
                onValueChange = { onAction(CalculatorAction.SetTitle(it)) },
                label = if (state.title.isEmpty()) {
                    { Text(text = "Title") }
                } else null,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        DateSection(
            title = "From",
            date = state.fromDateMillis.toFormattedDateString(),
            onDateIconClick = { onAction(CalculatorAction.ShowDatePicker(DateField.FROM)) }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        DateSection(
            title = "To",
            date = state.toDateMillis.toFormattedDateString(),
            onDateIconClick = { onAction(CalculatorAction.ShowDatePicker(DateField.TO)) }
        )
    }
}

@Composable
private fun StatisticsSection(
    modifier: Modifier = Modifier,
    state: CalculatorUiState
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgeBoxSection(
            title = "Time Passed",
            values = listOf(
                "YEARS" to state.period.years.toString(),
                "MONTHS" to state.period.months.toString(),
                "DAYS" to state.period.days.toString()
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        AgeBoxSection(
            title = "Upcoming",
            values = listOf(
                "MONTHS" to "12",
                "DAYS" to "04"
            )
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        StatisticsCard(
            title = "Statistics",
            ageStats = state.ageStats
        )
    }
}

@Composable
private fun DateSection(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    onDateIconClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = date)
        IconButton(onClick = onDateIconClick) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Calender"
            )
        }
    }
}

@PreviewScreenSizes
@Composable
private fun PreviewCalculatorScreen() {
    AgeCalculatorTheme {
        CalculatorScreen(
            state = CalculatorUiState(),
            onAction = {},
            navigateUp = {},
            event = emptyFlow()
        )
    }
}