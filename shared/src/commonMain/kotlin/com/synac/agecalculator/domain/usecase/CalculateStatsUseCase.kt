package com.synac.agecalculator.domain.usecase

import com.synac.agecalculator.domain.model.AgeStats
import com.synac.agecalculator.domain.model.CalculationResult
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.until
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class CalculateStatsUseCase {

    @OptIn(ExperimentalTime::class)
    operator fun invoke(fromDateMillis: Long?, toDateMillis: Long?): CalculationResult {
        val from = fromDateMillis ?: System.currentTimeMillis()
        val to = toDateMillis ?: System.currentTimeMillis()

        val timeZone = TimeZone.currentSystemDefault()
        val fromInstant = Instant.fromEpochMilliseconds(from)
        val toInstant = Instant.fromEpochMilliseconds(to)

        val period = fromInstant.periodUntil(toInstant, timeZone)
        val diffInMonths = fromInstant.until(toInstant, DateTimeUnit.MONTH, timeZone)
        val diffInWeeks = fromInstant.until(toInstant, DateTimeUnit.WEEK, timeZone)
        val diffInDays = fromInstant.until(toInstant, DateTimeUnit.DAY, timeZone)
        val diffInHours = fromInstant.until(toInstant, DateTimeUnit.HOUR, timeZone)
        val diffInMinutes = fromInstant.until(toInstant, DateTimeUnit.MINUTE, timeZone)
        val diffInSeconds = fromInstant.until(toInstant, DateTimeUnit.SECOND, timeZone)

        //Calculate next Anniversary
        val fromDate = fromInstant.toLocalDateTime(timeZone).date
        val toDate = toInstant.toLocalDateTime(timeZone).date

        var nextAnniversary = LocalDate(toDate.year, fromDate.month, fromDate.day)
        if (nextAnniversary < toDate) {
            nextAnniversary = LocalDate(toDate.year + 1, fromDate.month, fromDate.day)
        }

        val nextAnniversaryInstant = nextAnniversary.atStartOfDayIn(timeZone)
        val upcomingPeriod = toInstant.periodUntil(nextAnniversaryInstant, timeZone)

        return CalculationResult(
            passedPeriod = period,
            upcomingPeriod = upcomingPeriod,
            ageStats = AgeStats(
                years = period.years,
                months = diffInMonths.toInt(),
                weeks = diffInWeeks.toInt(),
                days = diffInDays.toInt(),
                hours = diffInHours.toInt(),
                minutes = diffInMinutes.toInt(),
                seconds = diffInSeconds.toInt()
            )
        )
    }
}