package com.who.hydratemate.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

import com.google.ai.client.generativeai.GenerativeModel
import com.who.hydratemate.BuildConfig
import com.who.hydratemate.network.GeminiViewModel

suspend fun geminiScreen(): MutableState<List<String>> {
    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.apikey
    )

    val prompt =
        "Generate 10 funny messages, separated by a new line, each message 10 words or less" +
                " for reminding someone to drink water. Don't use any symbol like '-' or numbers at the start of the message." +
                "Each message should be in a new line."

    val response: String = try {
        val newResponse = generativeModel.generateContent(prompt)
        newResponse.text.toString()
    } catch (e: Exception) {
        "Error: ${e.message}"
    }

    return mutableStateOf(response.split("\n"))
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview
@Composable
fun GeminiScreen() {

    val viewModel = hiltViewModel<GeminiViewModel>()

    val responseList by viewModel.responseList.collectAsState(
        emptyList()
    )
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Gemini Screen")
            if(responseList.isEmpty()){
                Text("Loading...")
            }
            LazyColumn {
                items(responseList) { response ->
                    Text(response)
                }
            }
        }
    }
}

