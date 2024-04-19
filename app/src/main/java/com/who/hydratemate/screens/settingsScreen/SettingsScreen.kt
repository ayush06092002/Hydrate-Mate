package com.who.hydratemate.screens.settingsScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.who.hydratemate.models.Settings
import com.who.hydratemate.utils.Converters
import com.who.hydratemate.utils.fontFamily
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel
) {
    TimeSettings(settingsViewModel)
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun TimeSettings(settingsViewModel: SettingsViewModel) {
    var settingsItem: Settings? by remember {
        mutableStateOf(null)
    }
    val settingsList = remember {
        settingsViewModel.settingsList
    }
    val size = remember {
        settingsList.value.size
    }
    val pickedWakeUpTime = remember {
        if(settingsList.value.isNotEmpty()) {
            mutableStateOf(Converters._epochToLocalDateTime(settingsList.value[size - 1].wakeUpTime))
        } else {
            mutableStateOf(LocalDateTime.now())
        }
    }
    val pickedSleepTime = remember {
        if (settingsList.value.isNotEmpty()) {
            mutableStateOf(Converters._epochToLocalDateTime(settingsList.value[size - 1].sleepTime))
        } else {
            mutableStateOf(LocalDateTime.now())
        }
    }
    val formattedWakeUpTime by remember {
        derivedStateOf {
            pickedWakeUpTime.value.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
    val formattedSleepTime by remember {
        derivedStateOf {
            pickedSleepTime.value.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
    val wakeTimeDialogState = rememberMaterialDialogState()
    val sleepTimeDialogState = rememberMaterialDialogState()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .padding(top = 60.dp)
                .width(300.dp)
                .height(500.dp),
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
                Divider(modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))

                Text(
                    text = "Wake Up Time: $formattedWakeUpTime",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier.padding(5.dp),
                    color = Color.Black
                )

                CreateSetButton(text = "Set Wake Time") {
                    wakeTimeDialogState.show()
                }

                Text(
                    text = "Sleep Time: $formattedSleepTime",
                    style = TextStyle(
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        fontFamily = fontFamily
                    ),
                    modifier = Modifier.padding(5.dp),
                    color = Color.Black
                )

                CreateSetButton(text = "Set Sleep Time") {
                    sleepTimeDialogState.show()
                }

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
                        initialTime = pickedWakeUpTime.value.toLocalTime(),
                        title = "Set Wake Up Time",
                        onTimeChange = {
                            pickedWakeUpTime.value = it.atDate(pickedWakeUpTime.value.toLocalDate())
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
                        initialTime = pickedSleepTime.value.toLocalTime(),
                        title = "Set Sleep Time",
                        onTimeChange = {
                            pickedSleepTime.value = it.atDate(pickedSleepTime.value.toLocalDate())
                        }
                    )
                }
                var selectedInterval by remember {
                    if(settingsList.value.isNotEmpty()) {
                        mutableLongStateOf(settingsList.value[size - 1].reminderInterval)
                    } else {
                        mutableLongStateOf(0L)
                    }
                }

                MyExposedDropdownMenuBox(selectedInterval) {
                    selectedInterval = it
                }
                Button(onClick = {
                    settingsItem = Settings(
                        wakeUpTime = Converters.localDateTimeToEpoch(pickedWakeUpTime.value),
                        sleepTime = Converters.localDateTimeToEpoch(pickedSleepTime.value),
                        dailyGoalComplete = false,
                        reminderInterval = selectedInterval
                    )
                    settingsViewModel.insertSettings(settingsItem!!)

                },
                    modifier = Modifier
                        .padding(5.dp)
                        .size(100.dp, 50.dp),
                )
                {
                    Text(
                        text = "Save",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            fontFamily = fontFamily
                        )
                    )
                }


            }
        }
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
            containerColor = Color(0xFFADE8F4)
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


fun formatInterval(interval: Long): String {
    return when (interval) {
        0L -> "None"
        15L -> "15 minutes"
        30L -> "30 minutes"
        60L -> "1 hour"
        90L -> "1.5 hours"
        120L -> "2 hours"
        else -> "Unknown"
    }
}


@Composable
fun MyExposedDropdownMenuBox(
    interval: Long,
    fetchInterval: (Long) -> Unit
) {
    var selectedInterval by remember {
        mutableLongStateOf(interval)
    }

    val reminderIntervals = listOf(
        30L,
        60L,
        90L,
        120L
    )
    var expandedState by remember { mutableStateOf(false) }

    var displayText by remember {
        if (interval == 0L) {
            mutableStateOf("Select Interval")
        } else {
            mutableStateOf(formatInterval(interval))
        }
    }
    Box(
        modifier = Modifier
            .padding(5.dp)
            .size(150.dp, 35.dp)
            .border(1.dp, Color(0xFFADE8F4), shape = RoundedCornerShape(4.dp))
            .clickable {
                expandedState = !expandedState
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = displayText,
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    fontFamily = fontFamily
                ),
                color = Color.Black
            )

            DropdownMenu(
                expanded = expandedState,
                onDismissRequest = {
                    expandedState = false
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                reminderIntervals.forEach { interval ->
                    DropdownMenuItem(
                        text = { Text(text = formatInterval(interval)) },
                        onClick = {
                            selectedInterval = interval
                            displayText = formatInterval(interval)
                            fetchInterval(interval)
                            expandedState = false
                        }
                    )
                }
            }
        }
    }
}

