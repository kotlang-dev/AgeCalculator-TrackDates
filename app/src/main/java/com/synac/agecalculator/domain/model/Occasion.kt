package com.synac.agecalculator.domain.model

data class Occasion(
    val id: Int?,
    val title: String,
    val dateMillis: Long?,
    val emoji: String,
    val isReminderEnabled: Boolean
)
