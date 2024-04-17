package com.who.hydratemate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.who.hydratemate.models.Notifications
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications")
    fun getNotifications(): Flow<List<Notifications>>

    @Query("SELECT * FROM notifications WHERE time = :id")
    fun getNotificationById(id: Long): Flow<Notifications>

    @Query("DELETE FROM notifications WHERE time = :id")
    suspend fun deleteNotification(id: Long)

    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()

    @Query("SELECT COUNT(*) FROM notifications")
    fun getNotificationsCount(): Int

    @Query("SELECT COUNT(*) FROM notifications WHERE completed = 1")
    fun getCompletedNotificationsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notifications)

    @Query("UPDATE notifications SET completed = 1 WHERE time = :id")
    suspend fun markCompleted(id: Long)

    @Query("UPDATE notifications SET completed = 0 WHERE time = :id")
    suspend fun markIncomplete(id: Long)

}