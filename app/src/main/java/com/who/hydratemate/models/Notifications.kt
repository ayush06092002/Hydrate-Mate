package com.who.hydratemate.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "notifications")
data class Notifications(
    @PrimaryKey
    val time: Long,
    val title: String,
    val completed: Boolean
)
