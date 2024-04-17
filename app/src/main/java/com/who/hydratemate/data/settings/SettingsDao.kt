package com.who.hydratemate.data.settings

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.who.hydratemate.models.Settings
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings")
    fun getSettings(): Flow<List<Settings>>

    @Query("SELECT * FROM settings WHERE id = :id")
    fun getSettingsById(id: UUID): Flow<Settings>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: Settings)

    @Query("UPDATE settings SET wake_up_time = :wakeUpTime WHERE id = :id")
    fun updateWakeUpTime(id: UUID, wakeUpTime: Long)

    @Query("UPDATE settings SET sleep_time = :sleepTime WHERE id = :id")
    fun updateSleepTime(id: UUID, sleepTime: Long)

    @Query("UPDATE settings SET daily_goal = :dailyGoalComplete WHERE id = :id")
    fun updateDailyGoalComplete(id: UUID, dailyGoalComplete: Boolean)

    @Query("UPDATE settings SET reminder_interval = :reminderInterval WHERE id = :id")
    fun updateReminderInterval(id: UUID, reminderInterval: Long)

    @Query("SELECT COUNT(*) FROM settings WHERE daily_goal = 1")
    fun countDailyGoalComplete(): Int

}