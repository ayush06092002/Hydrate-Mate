package com.who.hydratemate.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.utils.Converters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notifications)

}