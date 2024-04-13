package com.who.hydratemate.screens.notiScreen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class NotificationScreenViewModel: ViewModel() {
    private val _notiStatus = mutableStateOf(false)
    val notiStatus: State<Boolean> = _notiStatus

    fun toggleNotiStatus() {
        _notiStatus.value = !_notiStatus.value
        Log.d("NotificationScreenViewModel", "Notification status: ${_notiStatus.value}")
    }
}