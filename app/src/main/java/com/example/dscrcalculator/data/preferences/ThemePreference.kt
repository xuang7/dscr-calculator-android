package com.example.dscrcalculator.data.preferences

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemePreference {
    private const val PREFS_NAME = "dscr_settings"
    private const val KEY_NIGHT_MODE = "night_mode"

    fun getNightMode(context: Context): Int {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

    fun setNightMode(context: Context, nightMode: Int) {
        val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putInt(KEY_NIGHT_MODE, nightMode) }
    }
}
