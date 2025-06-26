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
import timber.log.Timber
import java.util.Calendar
import java.util.concurrent.TimeUnit

class ReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    companion object {
        const val KEY_OCCASION_ID = "occasionId"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }

    override fun doWork(): Result {

        val occasionId = inputData.getInt(KEY_OCCASION_ID, -1)
        val title = inputData.getString(KEY_TITLE) ?: "Reminder"
        val message = inputData.getString(KEY_MESSAGE) ?: "You have an event today!"

        showNotification(title, message)

        if (occasionId != -1) {
            val calender = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                add(Calendar.YEAR, 1)
            }
            val nextDelay = calender.timeInMillis - System.currentTimeMillis()
            val nextData = workDataOf(
                KEY_OCCASION_ID to occasionId,
                KEY_TITLE to title,
                KEY_MESSAGE to message
            )

            val nextRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(nextDelay, TimeUnit.MILLISECONDS)
                .setInputData(nextData)
                .addTag("reminder_$occasionId")
                .build()

            WorkManager.getInstance(applicationContext).enqueue(nextRequest)
            Timber.d("Work enqueued: ${nextRequest.tags}")
        }

        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "reminder_channel"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Create Notification Channel
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