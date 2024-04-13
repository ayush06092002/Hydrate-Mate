package com.who.hydratemate.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.who.hydratemate.MainActivity
import com.who.hydratemate.R
import kotlin.random.Random

class NotificationService(
    private val context: Context
) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showWaterNotification() {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(context, "hydratemate_channel")
            .setContentTitle("Drink Water")
            .setContentText("It's time to drink water")
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
}