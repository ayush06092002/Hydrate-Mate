package com.who.hydratemate.screens.homeScreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import com.who.hydratemate.utils.fontFamily
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel()
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
        Box(modifier = Modifier
            .padding(25.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .width(500.dp)
            .height(450.dp),
            contentAlignment = Alignment.Center
        )
        {
            val progressState by
            animateFloatAsState(targetValue = if (viewModel.showProgress.value) 80f else 0f,
                label = "progressState",
                animationSpec = tween(1500, easing = LinearEasing))
            CircularProgressBar(
                modifier = Modifier.size(240.dp),
                progress = progressState,
                progressMax = 100f,
                progressBarColor = Color(0xFF48CAE4),
                progressBarWidth = 20.dp,
                backgroundProgressBarColor = Color(0xFFF5F5F6),
                backgroundProgressBarWidth = 10.dp,
                roundBorder = true,
                startAngle = 180f
            )

            Button(onClick = {
                viewModel.toggleShowProgress()
            }) {
                Text(
                    text = "Check Progress",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                    ),
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}