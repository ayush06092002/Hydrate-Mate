package com.who.hydratemate.screens.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.who.hydratemate.models.Settings
import com.who.hydratemate.utils.fontFamily
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
) {
    TimeSettings(settingsViewModel)
}

@Composable
private fun TimeSettings(settingsViewModel: SettingsViewModel) {
    var settingsItem: Settings? by remember {
        mutableStateOf(null)
    }
    var pickedWakeUpTime by remember {
        mutableStateOf(LocalTime.now())
    }
    var pickedSleepTime by remember {
        mutableStateOf(LocalTime.MIDNIGHT)
    }
    val formattedWakeUpTime by remember {
        derivedStateOf { //24 hour format
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedWakeUpTime)
        }
    }
    val formattedSleepTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm a")
                .format(pickedSleepTime)
        }
    }
    val wakeTimeDialogState = rememberMaterialDialogState()
    val sleepTimeDialogState = rememberMaterialDialogState()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .padding(top = 30.dp)
                .width(300.dp)
                .height(300.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFFFF)
            )
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Settings",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = fontFamily
                    ),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 16.dp, bottom = 5.dp)
                )
                Text(
                    text = "Set your preferences here!",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        fontFamily = fontFamily
                    ),
                    color = Color(0xFF90E0EF),
                    modifier = Modifier
                )
                Divider(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp))

                Text(
                    text = "Wake Up Time: $formattedWakeUpTime",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier.padding(5.dp)
                )

                CreateSetButton(text = "Set Wake Up Time") {
                    wakeTimeDialogState.show()
                }

                Text(
                    text = "Sleep Time: $formattedSleepTime",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier.padding(5.dp)
                )

                CreateSetButton(text = "Set Sleep Time") {
                    sleepTimeDialogState.show()
                }

                CreateMaterialDialog(
                    wakeTimeDialogState,
                    pickedWakeUpTime,
                    sleepTimeDialogState,
                    pickedSleepTime
                )

            }
        }
    }
}

@Composable
private fun CreateMaterialDialog(
    wakeTimeDialogState: MaterialDialogState,
    pickedWakeUpTime: LocalTime,
    sleepTimeDialogState: MaterialDialogState,
    pickedSleepTime: LocalTime
) {
    var pickedWakeUpTime1 = pickedWakeUpTime
    var pickedSleepTime1 = pickedSleepTime
    MaterialDialog(
        dialogState = wakeTimeDialogState,
        buttons = {
            positiveButton("OK") {
                wakeTimeDialogState.hide()
            }
            negativeButton("Cancel") {
                wakeTimeDialogState.hide()
            }
        }
    ) {
        timepicker(
            initialTime = pickedWakeUpTime1,
            title = "Set Wake Up Time",
            onTimeChange = {
                pickedWakeUpTime1 = it
            }
        )
    }

    MaterialDialog(
        dialogState = sleepTimeDialogState,
        buttons = {
            positiveButton("OK") {
                sleepTimeDialogState.hide()
            }
            negativeButton("Cancel") {
                sleepTimeDialogState.hide()
            }
        }
    ) {
        timepicker(
            initialTime = pickedSleepTime1,
            title = "Set Sleep Time",
            onTimeChange = {
                pickedSleepTime1 = it
            }
        )
    }
}

@Composable
fun CreateSetButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(5.dp)
            .size(150.dp, 35.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0096C7)
        )
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                fontFamily = fontFamily
            )
        )
    }
}