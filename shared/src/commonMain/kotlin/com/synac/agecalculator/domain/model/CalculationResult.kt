package com.synac.agecalculator.domain.model

import kotlinx.datetime.DateTimePeriod

data class CalculationResult(
    val passedPeriod: DateTimePeriod,
    val upcomingPeriod: DateTimePeriod,
    val ageStats: AgeStats
)