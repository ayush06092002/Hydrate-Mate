package com.who.hydratemate.screens.HomeScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class HomeScreenViewModel: ViewModel() {
    private val _showProgress = mutableStateOf(false)
    val showProgress: State<Boolean> = _showProgress

    fun toggleShowProgress() {
        _showProgress.value = !_showProgress.value
    }
}