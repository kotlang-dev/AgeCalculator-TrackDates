package com.synac.agecalculator.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.synac.agecalculator.presentation.theme.NotificationActive
import com.synac.agecalculator.presentation.theme.NotificationOff

@Composable
fun ReminderIcon(
    modifier: Modifier = Modifier,
    isReminderEnabled: Boolean
) {
    val notificationIcon = if (isReminderEnabled) {
        Icons.Filled.NotificationActive
    } else Icons.Filled.NotificationOff
    Icon(
        modifier = modifier,
        imageVector = notificationIcon,
        contentDescription = "Set Notification"
    )
}