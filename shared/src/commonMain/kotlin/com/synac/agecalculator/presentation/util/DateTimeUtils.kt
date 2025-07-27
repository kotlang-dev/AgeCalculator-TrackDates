package com.synac.agecalculator.presentation.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime

fun Long?.toFormattedDateString(): String {
    val instant = Instant.fromEpochMilliseconds(this ?: System.currentTimeMillis())
    val date = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        char(' ')
        dayOfMonth()
        char(',')
        char(' ')
        year()
    }
    return formatter.format(date)
}

fun Long?.periodUntil(): DateTimePeriod {
    val timeZone = TimeZone.currentSystemDefault()
    val fromInstant = Instant.fromEpochMilliseconds(this ?: System.currentTimeMillis())
    val toInstant = Clock.System.now()

    return fromInstant.periodUntil(toInstant, timeZone)
}