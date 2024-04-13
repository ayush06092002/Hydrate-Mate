package com.who.hydratemate.data

import com.who.hydratemate.models.Notifications


interface NotificationScheduler {
    fun schedule(item: Notifications)
    fun cancel(item: Notifications)
}