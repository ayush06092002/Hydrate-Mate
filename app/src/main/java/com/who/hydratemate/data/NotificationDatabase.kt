package com.who.hydratemate.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.who.hydratemate.models.Notifications

@Database(entities = [Notifications::class], version = 1, exportSchema = false)
abstract class NotificationDatabase : RoomDatabase(){
    abstract fun notificationDao(): NotificationDao
}