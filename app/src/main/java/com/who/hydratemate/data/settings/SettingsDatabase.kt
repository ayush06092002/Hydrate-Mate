package com.who.hydratemate.data.settings

import androidx.room.Database
import androidx.room.RoomDatabase
import com.who.hydratemate.models.Settings

@Database(entities = [Settings::class], version = 1, exportSchema = false)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}