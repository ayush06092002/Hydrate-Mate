package com.who.hydratemate.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "settings")
data class Settings(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "wake_up_time")
    val wakeUpTime: Long,
    @ColumnInfo(name = "sleep_time")
    val sleepTime: Long,
    @ColumnInfo(name = "daily_goal")
    val dailyGoalComplete: Boolean,
    @ColumnInfo(name = "reminder_interval")
    val reminderInterval: Long
)