package com.who.hydratemate.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity(tableName = "notifications")
data class Notifications(
    @PrimaryKey
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "message")
    val completed: Boolean
)
