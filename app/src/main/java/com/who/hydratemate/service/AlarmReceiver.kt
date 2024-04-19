package com.who.hydratemate.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("notification")?: return
        val time = intent.getLongExtra("time", 0)
        val notificationService = NotificationService(context!!)
        Log.d("AlarmReceiver", "Received notification: $message")
        notificationService.showWaterNotification(message, time)
    }
}