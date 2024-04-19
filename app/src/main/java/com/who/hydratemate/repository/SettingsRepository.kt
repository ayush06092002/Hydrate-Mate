package com.who.hydratemate.repository

import com.who.hydratemate.data.settings.SettingsDao
import com.who.hydratemate.models.Settings
import java.util.UUID
import javax.inject.Inject

class SettingsRepository @Inject constructor(private val settingsDao: SettingsDao) {

    fun getSettings() = settingsDao.getSettings()

    fun getSettingsById(id: UUID) = settingsDao.getSettingsById(id)

    suspend fun insertSettings(settings: Settings) = settingsDao.insertSettings(settings)

    fun updateWakeUpTime(id: UUID, wakeUpTime: Long) = settingsDao.updateWakeUpTime(id, wakeUpTime)

    suspend fun deleteAllSettings() = settingsDao.deleteAllSettings()

    fun updateSleepTime(id: UUID, sleepTime: Long) = settingsDao.updateSleepTime(id, sleepTime)

    fun updateDailyGoalComplete(id: UUID) = settingsDao.updateDailyGoalComplete(id)

    fun updateReminderInterval(id: UUID, reminderInterval: Long) = settingsDao.updateReminderInterval(id, reminderInterval)

    fun countDailyGoalComplete() = settingsDao.countDailyGoalComplete()

}