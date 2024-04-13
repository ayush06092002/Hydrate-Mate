package com.who.hydratemate.screens.notiScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.who.hydratemate.data.AlarmItem
import com.who.hydratemate.data.AndroidAlarmScheduler
import com.who.hydratemate.data.NotificationViewModel
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.service.NotificationService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationScreen(
//    notificationViewModel: NotificationViewModel
) {
    val notificationPermission = rememberPermissionState(
        permission = android.Manifest.permission.POST_NOTIFICATIONS
    )
    val scheduler = AndroidAlarmScheduler(LocalContext.current)
    var alarmItem: AlarmItem? = null
    LaunchedEffect(key1 = true) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }
    }
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var secondsText by remember {
            mutableStateOf("")
        }

        var messageText by remember {
            mutableStateOf("")
        }

        OutlinedTextField(value = secondsText, onValueChange = {
            secondsText = it
        }, label = {
            Text("Seconds")
        },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )
        OutlinedTextField(value = messageText, onValueChange = {
            messageText = it
        }, label = {
            Text("Message")
        },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            Button(onClick = {
//                notificationViewModel.insertNotification(Notifications(
//                    time = LocalDateTime.now()
//                        .plusSeconds(secondsText.toLong())
//                        .atZone(ZoneId.systemDefault())
//                        .toInstant()
//                        .toEpochMilli(),
//                    title = messageText,
//                    completed = false
//                ))
//                scheduler.schedule(Notifications(
//                    time = LocalDateTime.now()
//                        .plusSeconds(secondsText.toLong())
//                        .atZone(ZoneId.systemDefault())
//                        .toInstant()
//                        .toEpochMilli(),
//                    title = messageText,
//                    completed = false
//                ))
//                secondsText = ""
//                messageText = ""
                Log.d("NotificationScreen", "Notification scheduled for $secondsText seconds from now.")
                alarmItem = AlarmItem(
                    time = LocalDateTime.now()
                        .plusSeconds(secondsText.toLong()),
                    title = messageText
                )
                alarmItem?.let(scheduler::schedule)
                secondsText = ""
                messageText = ""
            }, modifier = Modifier.padding(16.dp)) {
                Text("Schedule Notification")
            }

            Button(onClick = {
                alarmItem?.let(scheduler::cancel)
                secondsText = ""
                messageText = ""
            },
                modifier = Modifier.padding(16.dp)) {
                Text("Cancel Notification")
                }
        }
    }
}