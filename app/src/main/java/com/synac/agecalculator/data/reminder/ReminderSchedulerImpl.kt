package com.synac.agecalculator.data.reminder

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.synac.agecalculator.domain.model.Occasion
import com.synac.agecalculator.domain.repository.ReminderScheduler
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderSchedulerImpl(
    private val workManager: WorkManager
) : ReminderScheduler {

    override fun schedule(occasion: Occasion) {
        val now = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = occasion.dateMillis ?: now
            set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        }

        // If the date this year has passed, move to next year
        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.YEAR, 1)
        }

        val delay = calendar.timeInMillis - now

        val data = workDataOf(
            "occasionId" to occasion.id,
            "title" to "Upcoming Occasion 🎉",
            "message" to "${occasion.title} is today!",
            "isRepeating" to true
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("reminder_${occasion.id}")
            .build()

        workManager.enqueue(request)
    }

    override fun cancel(occasionId: Int) {
        workManager.cancelAllWorkByTag("reminder_$occasionId")
    }

    fun testReminder() {
        val data = workDataOf(
            "title" to "Test Notification 🎯",
            "message" to "This is a test reminder triggered by WorkManager."
        )

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInputData(data)
            .build()

        workManager.enqueue(request)
    }
}