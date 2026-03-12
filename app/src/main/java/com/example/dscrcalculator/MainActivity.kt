package com.example.dscrcalculator

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatActivity
import com.example.dscrcalculator.data.preferences.ThemePreference
import com.example.dscrcalculator.ui.DSCRApp
import com.example.dscrcalculator.ui.theme.DSCRCalculatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(ThemePreference.getNightMode(this))
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DSCRCalculatorTheme {
                DSCRApp()
            }
        }
    }
}
