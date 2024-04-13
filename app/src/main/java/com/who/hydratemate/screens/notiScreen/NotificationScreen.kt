package com.who.hydratemate.screens.notiScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationScreenViewModel = viewModel()
) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val notification = remember {
            mutableStateOf("")
        }
        notification.value = if (viewModel.notiStatus.value) "Notifications are ON" else "Notifications are OFF"

        Text(text = notification.value)
        Button(onClick = {
            viewModel.toggleNotiStatus()
            Log.d("NotificationScreen", "Notification status: ${notification.value}")
        }) {
            Text(text = "Toggle Notifications")
        }
    }
}