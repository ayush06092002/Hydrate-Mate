package com.who.hydratemate.screens.homeScreen

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import com.who.hydratemate.R
import com.who.hydratemate.data.AndroidAlarmScheduler
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.models.Settings
import com.who.hydratemate.screens.notiScreen.NotificationViewModel
import com.who.hydratemate.screens.settingsScreen.SettingsViewModel
import com.who.hydratemate.service.NotificationService
import com.who.hydratemate.sharedPreferences.WeeklyGoalStatus
import com.who.hydratemate.utils.Converters
import com.who.hydratemate.utils.fontFamily
import com.who.hydratemate.utils.loadImageFromAssets
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun HomeScreen(
    notificationViewModel: NotificationViewModel,
    settingsViewModel: SettingsViewModel,
    openFromNotification: Boolean,
    time: Long
) {
    if(openFromNotification){
        if(notificationViewModel.scheduleList.value.contains(Notifications(time, String(), false))){
            Log.d("HomeScreen", "Marking notification as completed: ${Converters.epochToLocalDateTime(time)}")
            notificationViewModel.markCompleted(time)
        }
    }
    val notificationService= NotificationService(LocalContext.current)
    notificationService.scheduleMorningNotification(8, 0, "Good Morning. Tap on me to schedule your notifications for the day!")
    val notificationPermission = rememberPermissionState(
        permission = Manifest.permission.POST_NOTIFICATIONS
    )
    LaunchedEffect(key1 = true) {
        if (!notificationPermission.status.isGranted) {
            notificationPermission.launchPermissionRequest()
        }
    }
    CheckAndAddNotifications(notificationViewModel, settingsViewModel)
    Column(
        modifier = Modifier
            .padding(top = 20.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily,
            ),
            fontSize = 24.sp,
            color = Color.Black
        )
        ProgressStatus(notificationViewModel)

        NotificationHistory(notificationViewModel)
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CheckAndAddNotifications(
    notificationViewModel: NotificationViewModel,
    settingsViewModel: SettingsViewModel
) {
    val currentDate = Converters.epochToLocalDate(
        LocalDateTime.now()
        .atZone(ZoneId.systemDefault()).plusDays(0)
            .toInstant().toEpochMilli()
    )
    val list = remember {
        notificationViewModel.scheduleList
    }
    val totalNotifications = list.value.size
    val settings = remember {
        settingsViewModel.settingsList
    }
    if(settings.value.isEmpty()){
        Toast.makeText(
            LocalContext.current,
            "Please set your wake up and sleep time in settings",
            Toast.LENGTH_SHORT
        ).show()
        return
    }
    val settingsSize = settings.value.size
    val scheduler = AndroidAlarmScheduler(LocalContext.current)
    val notificationDate: String? = if (settings.value.isNotEmpty()) {
        Converters.epochToLocalDate(settings.value[settingsSize - 1].wakeUpTime)
    } else {
        null
    }
    Log.d("HomeScreen", "Current date: $currentDate, Notification date: $notificationDate")
    if (notificationDate != null && currentDate != notificationDate) {
        CheckForDailyCompletedGoal(notificationViewModel = notificationViewModel)
        Log.d("HomeScreen", "Deleting notifications")
        val completed = notificationViewModel.completedNotificationsCount.value

        if(completed == totalNotifications && settings.value.isNotEmpty()){
            Log.d("HomeScreen", "All notifications completed")
            settingsViewModel.updateDailyGoalComplete(settings.value[settingsSize - 1].id)
        }
        else{
            Log.d("HomeScreen", "Not all notifications completed")
            scheduler.schedule(
                Notifications(
                    time = LocalDateTime.now()
                        .plusSeconds(5)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli(),
                    message = "You did not complete all your goals yesterday. Try again today!",
                    completed = false
                )
            )
        }
        notificationViewModel.deleteAllNotifications()
        if(settings.value.isEmpty()){
            return
        }
        val wakeUpTime = Converters._epochToLocalDateTime(settings.value[settingsSize - 1].wakeUpTime)
        val wakeUpDate = LocalDateTime.now().toLocalDate()

        val sleepDateTime = Converters._epochToLocalDateTime(settings.value[settingsSize - 1].sleepTime)
        val reminderInterval = settings.value[settingsSize - 1].reminderInterval
        settingsViewModel.deleteAllSettings()
        Log.d("HomeScreen", "Settings Deleted $currentDate $notificationDate")
        val wakeUpDateTime = LocalDateTime.of(
            wakeUpDate,
            wakeUpTime.toLocalTime()
        )
        settingsViewModel.insertSettings(
            Settings(
                wakeUpTime = Converters.localDateTimeToEpoch(wakeUpDateTime),
                sleepTime = Converters.localDateTimeToEpoch(sleepDateTime),
                reminderInterval = reminderInterval,
                dailyGoalComplete = false
            )
        )
        AutoNotificationGenerator(
            wakeUpDateTime,
            sleepDateTime,
            reminderInterval,
            settings,
            settingsSize,
            notificationViewModel,
            scheduler
        )
    }
    if(list.value.isEmpty()){
        Log.d("HomeScreen", "No Notifications found")
        val wakeUpDateTime = Converters._epochToLocalDateTime(settings.value[settingsSize - 1].wakeUpTime)
        val sleepDateTime = Converters._epochToLocalDateTime(settings.value[settingsSize - 1].sleepTime)

        AutoNotificationGenerator(
            wakeUpDateTime,
            sleepDateTime,
            settings.value[settingsSize - 1].reminderInterval,
            settings,
            settingsSize,
            notificationViewModel,
            scheduler
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun AutoNotificationGenerator(
    wakeUpDateTime: LocalDateTime,
    sleepDateTime: LocalDateTime,
    reminderInterval: Long,
    settings: StateFlow<List<Settings>>,
    settingsSize: Int,
    notificationViewModel: NotificationViewModel,
    scheduler: AndroidAlarmScheduler
) {
    val interval = Duration.between(wakeUpDateTime, sleepDateTime).toMinutes() / reminderInterval

    if (interval > 0) { // Ensure interval is not zero
        for (i in 0 until interval.toInt()) {
            // Calculate notification time at each interval
            val notificationTime =
                wakeUpDateTime.plusMinutes(i * settings.value[settingsSize - 1].reminderInterval)

            // Convert notification time to epoch milliseconds
            val notificationMillis =
                notificationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            // Create and insert notification
            val notification = Notifications(
                time = notificationMillis,
                message = "Drink water",
                completed = false
            )
            notificationViewModel.insertNotification(notification)

            scheduler.schedule(notification)
        }
    }

    Toast.makeText(
        LocalContext.current,
        "Notifications scheduled successfully",
        Toast.LENGTH_SHORT
    ).show()
}

@Composable
fun CheckForDailyCompletedGoal(
    notificationViewModel: NotificationViewModel
) {
    val currentTimeMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(0) //for testing purpose
    WeeklyGoalStatus.CheckIfSunday(
        LocalContext.current,
        currentTimeMillis
    ){
        Log.d("HomeScreen", "Weekly goal completed")
        val scheduler = AndroidAlarmScheduler(LocalContext.current)
        val notification = Notifications(
            time = LocalDateTime.now()
                .plusSeconds(5)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli(),
            message = "Daily goal completed",
            completed = false
        )
        scheduler.schedule(notification)
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NotificationHistory(notificationViewModel: NotificationViewModel) {
    val notificationHistory by remember { mutableStateOf("No notifications yet") }
    val list = remember {
        notificationViewModel.scheduleList
    }
    var currTime by remember {
        mutableLongStateOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
    }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }
    }
    Box(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp, bottom = 70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .width(500.dp)
            .fillMaxHeight(),
    ){
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "History",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                ),
                fontSize = 18.sp,
                color = Color(0xFF0096C7)
            )
            Divider(modifier = Modifier.padding(top = 5.dp))

            if(list.value.isEmpty() || (list.value.isNotEmpty() && list.value[0].time > currTime)){
                Text(
                    text = notificationHistory,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    ),
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
            LazyColumn {
                items(list.value.size) { index ->
                    if(list.value[index].time > currTime){
                        return@items
                    }
                    var iconId by remember {
                        if(list.value[index].completed){
                            mutableIntStateOf(R.drawable.checked)
                        }else{
                            mutableIntStateOf(R.drawable.unchecked)
                        }
                    }

                    Row(modifier = Modifier
                        .padding(15.dp)
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically){
                        Image(bitmap = loadImageFromAssets(LocalContext.current,
                            "water_bottle.png").asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(50.dp))

                        Column(modifier = Modifier
                            .padding(7.dp)
                            .width(150.dp)){
                            Text(
                                text = list.value[index].message,
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = fontFamily,
                                ),
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            Text(text = Converters.epochToLocalDateTime(list.value[index].time),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = fontFamily,
                                ),
                                fontSize = 14.sp,
                                color = Color.Black
                            )
                        }
                        Icon(
                            painter = painterResource(id = iconId),
                            contentDescription = null,
                            tint = Color(0xFF0096C7),
                            modifier = Modifier
                                .padding(start = 50.dp)
                                .clickable {
                                    if (iconId == R.drawable.unchecked) {
                                        iconId = R.drawable.checked
                                        notificationViewModel.markCompleted(list.value[index].time)
                                        notificationViewModel.getCompletedNotificationsCount()
                                    } else {
                                        iconId = R.drawable.unchecked
                                        notificationViewModel.markIncomplete(list.value[index].time)
                                        notificationViewModel.getCompletedNotificationsCount()
                                    }
                                }
                            )
                    }
                    Divider()
                }
            }
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProgressStatus(notificationViewModel: NotificationViewModel) {
    Box(
        modifier = Modifier
            .padding(25.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .width(500.dp)
            .height(450.dp),
        contentAlignment = Alignment.Center
    )
    {
        NotificationsProgressBar(notificationViewModel = notificationViewModel){
            Text(
                text = "Goal Completed: ${it}%",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = fontFamily,
                ),
                fontSize = 18.sp,
                color = Color(0xFF48CAE4),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun NotificationsProgressBar(notificationViewModel: NotificationViewModel, percentage: @Composable (Int) -> Unit) {
    val completedNotificationsCount by notificationViewModel.completedNotificationsCount.observeAsState(initial = 0)
    val maxNotification = notificationViewModel.scheduleList.value.size

    val animatedProgress = animateFloatAsState(
        targetValue = if (maxNotification == 0) 0f else completedNotificationsCount.toFloat() / maxNotification.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = LinearEasing), label = ""
    )

    LaunchedEffect(completedNotificationsCount) {
        notificationViewModel.getCompletedNotificationsCount()
    }

    CircularProgressBar(
        modifier = Modifier.size(240.dp),
        progress = animatedProgress.value * 100f,
        progressMax = 100f,
        progressBarColor = Color(0xFF48CAE4),
        progressBarWidth = 20.dp,
        backgroundProgressBarColor = Color(0xFFF5F5F6),
        backgroundProgressBarWidth = 10.dp,
        roundBorder = true,
        startAngle = 180f
    )

    percentage((animatedProgress.value * 100).toInt())

}
