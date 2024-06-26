package com.who.hydratemate

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.who.hydratemate.navigation.AppNavigation
import com.who.hydratemate.ui.theme.HydrateMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        var openFromNotification = false
        var time = 0L
        if(intent?.hasExtra("notification_time") == true){
            time = intent.getLongExtra("notification_time", 0)
            openFromNotification = true
        }
        setContent {
            HydrateMateTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFF5F5F6)
                    ) {
                        AppNavigation(openFromNotification, time)
                    }
                }
            }
        }
    }
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "hydratemate_channel",
            "HydrateMate Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
}




