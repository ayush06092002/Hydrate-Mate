package com.who.hydratemate.data

import com.who.hydratemate.models.Notifications

interface AlarmScheduler {
//    fun schedule(item: AlarmItem)
//    fun cancel(item: AlarmItem)
    fun schedule(item: Notifications)
    fun cancel(item: Notifications)
}