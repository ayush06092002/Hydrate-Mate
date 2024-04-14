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
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "message")
    var completed: Boolean
)
