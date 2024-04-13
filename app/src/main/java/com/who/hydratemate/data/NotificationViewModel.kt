package com.who.hydratemate.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: NotificationRepository): ViewModel() {
    private val _scheduleList = MutableStateFlow<List<Notifications>>(emptyList())
    val scheduleList = _scheduleList.asStateFlow()

    init{
        viewModelScope.launch(Dispatchers.IO){
            repository.getNotifications().distinctUntilChanged().collect{
                _scheduleList.value = it
            }
        }
    }

    fun getNotificationById(id: Long){
        viewModelScope.launch {
            repository.getNotificationById(id)
        }
    }

    fun deleteNotification(id: Long){
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }

    fun deleteAllNotifications(){
        viewModelScope.launch {
            repository.deleteAllNotifications()
        }
    }

    fun getNotificationsCount(){
        viewModelScope.launch {
            repository.getNotificationsCount()
        }
    }

    fun insertNotification(notification: Notifications){
        viewModelScope.launch {
            repository.insertNotification(notification)
        }
    }
}