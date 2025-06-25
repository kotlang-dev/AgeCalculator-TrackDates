package com.synac.agecalculator.presentation.theme

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.synac.agecalculator.R

val Icons.Filled.NotificationActive: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_notifications_active)

val Icons.Filled.NotificationOff: ImageVector
    @Composable
    get() = ImageVector.vectorResource(R.drawable.ic_notifications_off)