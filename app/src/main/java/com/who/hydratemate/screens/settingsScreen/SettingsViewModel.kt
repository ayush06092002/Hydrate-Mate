package com.who.hydratemate.screens.settingsScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.who.hydratemate.models.Settings
import com.who.hydratemate.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository): ViewModel() {

    private val _settingsList = MutableStateFlow(emptyList<Settings>())
    val settingsList = _settingsList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.getSettings().distinctUntilChanged().collect {
                if (it.isNotEmpty()) {
                    _settingsList.value = it
                }else{
                    Log.d("SettingsViewModel", "No settings found")
                }
            }
        }
    }

    fun getSettingsById(id: UUID) {
        viewModelScope.launch {
            settingsRepository.getSettingsById(id)
        }
    }
    fun insertSettings(settings: Settings) {
        viewModelScope.launch {
            settingsRepository.insertSettings(settings)
            Log.d("SettingsViewModel", "Settings inserted")
        }
    }

    fun updateWakeUpTime(id: UUID, wakeUpTime: Long) {
        viewModelScope.launch {
            settingsRepository.updateWakeUpTime(id, wakeUpTime)
        }
    }

    fun deleteAllSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            settingsRepository.deleteAllSettings()
        }
    }

    fun updateSleepTime(id: UUID, sleepTime: Long) {
        viewModelScope.launch {
            settingsRepository.updateSleepTime(id, sleepTime)
        }
    }

    fun updateDailyGoalComplete(id: UUID) {
        CoroutineScope(Dispatchers.IO).launch {
            settingsRepository.updateDailyGoalComplete(id)
        }
    }
    fun updateReminderInterval(id: UUID, reminderInterval: Long) {
        viewModelScope.launch {
            settingsRepository.updateReminderInterval(id, reminderInterval)
        }
    }
    private val _dailyGoalCompleteCount = MutableLiveData<Int>()
    val dailyGoalCompleteCount: LiveData<Int>
        get() = _dailyGoalCompleteCount

    fun countDailyGoalComplete() {
        CoroutineScope(Dispatchers.IO).launch {
            val count = settingsRepository.countDailyGoalComplete()
            _dailyGoalCompleteCount.postValue(count)
        }
    }

}