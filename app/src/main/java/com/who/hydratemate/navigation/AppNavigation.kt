package com.who.hydratemate.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.who.hydratemate.R
import com.who.hydratemate.screens.HomeScreen.HomeScreen
import com.who.hydratemate.screens.NotiScreen.NotificationScreen
import com.who.hydratemate.screens.SettingsScreen.SettingsScreen
import com.who.hydratemate.utils.noRippleClickable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val selectedIndex = remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
        containerColor = Color(0xFFF5F5F6),
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(64.dp),
                cornerRadius = shapeCornerRadius(30.dp),
                selectedIndex = selectedIndex.intValue,
                ballAnimation = Straight(tween(300)),
                indentAnimation = Height(tween(500)),
                barColor = Color(0xFF48CAE4),
                ballColor = Color(0xFF023E8A),
            ) {
                bottomBarItems.entries.forEach {item->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .noRippleClickable {
                            selectedIndex.intValue = item.ordinal
                            navController.navigate(
                                route = AppScreens.entries[selectedIndex.intValue].name,
                                builder = {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true // saveState is true by default
                                    }
                                    launchSingleTop = true // Reuse existing composable if it's already on the back stack
                                    restoreState = true // Restore state when re selecting a previously selected item
                                }
                            )
                        },
                        contentAlignment = Alignment.Center) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = item.iconId),
                            contentDescription = null,
                            tint = if (selectedIndex.intValue == item.ordinal) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    ){

    }


    NavHost(navController = navController, startDestination = AppScreens.Home.name) {
        composable(AppScreens.Home.name) {
            HomeScreen()
        }
        composable(AppScreens.Notifications.name) {
            NotificationScreen()
        }
        composable(AppScreens.Settings.name) {
            SettingsScreen()
        }

    }

}

enum class bottomBarItems(val iconId: Int){

    Home(iconId = R.drawable.home),
    Notifications(iconId = R.drawable.notification),
    Settings(iconId = R.drawable.settings);
}