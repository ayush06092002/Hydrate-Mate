package com.who.hydratemate.screens.homeScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import com.who.hydratemate.R
import com.who.hydratemate.screens.notiScreen.NotificationViewModel
import com.who.hydratemate.utils.Converters
import com.who.hydratemate.utils.fontFamily
import com.who.hydratemate.utils.loadImageFromAssets
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun HomeScreen(
    notificationViewModel: NotificationViewModel
) {
    Column(
        modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp),
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
fun NotificationHistory(notificationViewModel: NotificationViewModel) {
    var notificationHistory by remember { mutableStateOf("No notifications yet") }
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
            .padding(20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .width(500.dp)
            .height(300.dp),
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
            LaunchedEffect(key1 = true) {
                if(list.value.isNotEmpty()){
                    notificationHistory = ""
                }
            }
            if(list.value.isEmpty()){
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
                    Divider()                }
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

        val completedNotificationsCount by notificationViewModel.completedNotificationsCount.observeAsState(initial = 0)
        val maxNotification = notificationViewModel.scheduleList.value.size

        val animatedProgress = animateFloatAsState(
            targetValue = completedNotificationsCount.toFloat() / maxNotification.toFloat(),
            animationSpec = tween(durationMillis = 800, easing = LinearEasing), label = ""
        )

        LaunchedEffect(completedNotificationsCount) {
            notificationViewModel.getCompletedNotificationsCount()
        }

        Log.d("ProgressStatus", "Max: $maxNotification, Completed: $completedNotificationsCount")

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



    }
}