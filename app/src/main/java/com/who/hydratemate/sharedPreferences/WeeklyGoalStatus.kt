package com.who.hydratemate.sharedPreferences

import android.content.Context
import androidx.compose.runtime.Composable
import java.util.Calendar

object WeeklyGoalStatus {
    private const val COUNT_PREF_NAME = "weekly_goal_count_pref"
    private const val LAST_UPDATED_KEY = "last_updated"
    @Composable
    fun CheckIfSunday(
        context: Context,
        currentTimeMillis: Long?,
        function: @Composable () -> Unit
    ){
        val sharedPreferences = context.getSharedPreferences(COUNT_PREF_NAME, Context.MODE_PRIVATE)
        val lastUpdatedMillis = sharedPreferences.getLong(LAST_UPDATED_KEY, 0)
        val lastUpdatedCalendar = Calendar.getInstance().apply {
            timeInMillis = lastUpdatedMillis
        }
        val currentCalendar = Calendar.getInstance().apply {
            // Use provided current time if available, otherwise use current system time
            timeInMillis = currentTimeMillis ?: System.currentTimeMillis()
        }

        if (currentCalendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY &&
            currentCalendar.get(Calendar.WEEK_OF_YEAR) != lastUpdatedCalendar.get(Calendar.WEEK_OF_YEAR)) {
            // Update last updated time to current time
            sharedPreferences.edit().putLong(LAST_UPDATED_KEY, currentCalendar.timeInMillis).apply()
            function()
        }
    }
}