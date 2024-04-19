package com.who.hydratemate.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.who.hydratemate.MainActivity
import com.who.hydratemate.R
import java.util.Calendar
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showWaterNotification(message: String, time: Long) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("notification_time", time)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "hydratemate_channel")
            .setContentTitle("Drink Water")
            .setContentText(message)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }

    fun scheduleMorningNotification(hour: Int, minute: Int, message: String) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

        // Create a Calendar object to set the alarm time
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            // Ensure that if the specified time has already passed today, the alarm is set for the next day
            if (this.before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }

        // Create an intent for the morning notification
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("notification", message)
        }

        // Create a PendingIntent for the morning notification
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            MORNING_NOTIFICATION_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule the alarm to repeat daily
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    companion object {
        private const val MORNING_NOTIFICATION_REQUEST_CODE = 1001
    }
}