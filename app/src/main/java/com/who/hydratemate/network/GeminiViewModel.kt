package com.who.hydratemate.network

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.who.hydratemate.BuildConfig
import com.who.hydratemate.models.Notifications
import com.who.hydratemate.screens.geminiScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GeminiViewModel @Inject constructor() : ViewModel() {
    @SuppressLint("CoroutineCreationDuringComposition", "RememberReturnType")
    private val _responseList = MutableStateFlow<List<String>>(emptyList())
    val responseList = _responseList.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = BuildConfig.apikey
            )

            val prompt =
                "Generate 20 funny messages, separated by a new line, each message 10 words or less" +
                        " for reminding someone to drink water. Don't use any symbol like '-' or numbers at the start of the message." +
                        "Each message should be in a new line."

            val response: String = try {
                val newResponse = generativeModel.generateContent(prompt)
                newResponse.text.toString()
            } catch (e: Exception) {
                "Don't forget to hydrate, your body deserves it!\n" +
                "Water break: hydrate like your houseplants do!\n" +
                "Sip, sip, hooray! Time for a water party!\n" +
                "Hydration station: refill now for optimal performance!\n" +
                "Stay hydrated: it's the coolest trend since sliced bread.\n" +
                "Dive into hydration: it's like a pool party for your body!\n" +
                "Drink water like it's your superpower elixir.\n" +
                "H2Oh yeah! Time to quench that thirst, my friend!\n" +
                "Hydration reminder: your body is a water wonderland!\n" +
                "Thirsty? Water's got your back, always and forever.\n" +
                "Water: the ultimate sidekick in your daily adventures!\n" +
                "H2O, the elixir of life: bottoms up, my friend!\n" +
                "Hydrate or... wait, there's no 'or'. Just hydrate!\n" +
                "Water break: because even superheroes need to stay hydrated.\n" +
                "Splash into hydration mode: your body will thank you later!\n" +
                "Stay hydrated: your organs are cheering you on silently!\n" +
                "Remember, water is nature's way of high-fiving your insides!\n" +
                "Pour yourself a glass of water, then pat yourself.\n" +
                "Hydration check: water is your body's best friend forever!\n" +
                "Hydrate, hydrate, hydrate! Because who needs dryness anyway?"
            }

            Log.d("GeminiViewModel", response)

            _responseList.value = response.split("\n").shuffled()
        }
    }
    suspend fun waitForResponseList() {
        // Wait until responseList is not empty
        while (responseList.value.isEmpty()) {
            delay(2000)
        }
    }

}