package com.who.hydratemate.data

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.screens.notiScreen.NotificationViewModel
import com.who.hydratemate.service.AlarmReceiver

class AndroidAlarmScheduler(
    private val context: Context,
): NotificationScheduler {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    @SuppressLint("ShortAlarm")
    override fun schedule(item: Notifications) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("notification", item.message)
            putExtra("time", item.time)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: Notifications) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}