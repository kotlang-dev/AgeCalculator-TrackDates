package com.synac.agecalculator.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.synac.agecalculator.R

@Composable
fun ReminderIcon(
    modifier: Modifier = Modifier,
    isReminderEnabled: Boolean
) {
    val notificationIconResId = if (isReminderEnabled) {
        R.drawable.ic_notifications_active
    } else R.drawable.ic_notifications_off
    Icon(
        modifier = modifier,
        painter = painterResource(notificationIconResId),
        contentDescription = "Set Notification"
    )
}