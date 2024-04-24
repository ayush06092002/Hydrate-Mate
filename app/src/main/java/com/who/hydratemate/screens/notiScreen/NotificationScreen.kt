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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.who.hydratemate.data.AndroidAlarmScheduler
import com.who.hydratemate.models.Notifications
import java.time.LocalDateTime
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationScreen(
    notificationViewModel: NotificationViewModel
) {
    var notificationItem: Notifications? by remember{
        mutableStateOf(null)
    }

    val scheduler = AndroidAlarmScheduler(LocalContext.current)
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
                notificationItem = Notifications(
                    time = LocalDateTime.now()
                        .plusSeconds(secondsText.toLong())
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli(),
                    message = messageText,
                    completed = false
                )
                notificationViewModel.insertNotification(notificationItem!!)
                scheduler.schedule(notificationItem!!)
                Log.d("Notification", "Scheduled from notification screen")
                secondsText = ""
                messageText = ""
            }, modifier = Modifier.padding(16.dp)) {
                Text("Schedule Notification")
            }

            Button(onClick = {
                scheduler.cancel(notificationItem!!)
                secondsText = ""
                messageText = ""
            },
                modifier = Modifier.padding(16.dp)) {
                Text("Cancel Notification")
                }
        }
    }
}