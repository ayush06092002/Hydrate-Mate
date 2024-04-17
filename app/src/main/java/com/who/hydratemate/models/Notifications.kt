package com.who.hydratemate.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull


@Entity(tableName = "notifications")
data class Notifications(
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "time")
    var time: Long,
    @ColumnInfo(name = "message")
    var message: String,
    @ColumnInfo(name = "completed")
    var completed: Boolean
)
