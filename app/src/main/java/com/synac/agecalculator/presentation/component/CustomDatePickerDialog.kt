package com.synac.agecalculator.presentation.component

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerDialog(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: (Long?) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (isOpen) {
        DatePickerDialog(
            modifier = modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(
                    onClick = { onConfirmButtonClick(state.selectedDateMillis) }
                ) {
                    Text(text = "Select")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancel")
                }
            },
            content = {
                DatePicker(state = state)
            }
        )
    }
}