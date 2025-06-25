package com.synac.agecalculator.data.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.synac.agecalculator.R
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val occasionId = inputData.getInt("occasionId", -1)
        val isRepeating = inputData.getBoolean("isRepeating", false)

        val title = inputData.getString("title") ?: "Reminder"
        val message = inputData.getString("message") ?: "You have an event today!"

        showNotification(title, message)

        if (isRepeating && occasionId != -1) {
            val nextOccasionDateMillis = System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000
            val nextData = workDataOf(
                "occasionId" to occasionId,
                "title" to title,
                "message" to message,
                "isRepeating" to true
            )

            val nextRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(365, TimeUnit.DAYS)
                .setInputData(nextData)
                .addTag("reminder_$occasionId")
                .build()

            WorkManager.getInstance(applicationContext).enqueue(nextRequest)
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}