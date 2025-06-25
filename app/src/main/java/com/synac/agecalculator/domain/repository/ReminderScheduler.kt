package com.synac.agecalculator.domain.repository

import com.synac.agecalculator.domain.model.Occasion

interface ReminderScheduler {
    fun schedule(occasion: Occasion)
    fun cancel(occasionId: Int)
}
