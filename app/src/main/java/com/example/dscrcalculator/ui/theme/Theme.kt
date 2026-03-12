package com.example.dscrcalculator.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    onPrimary = Color(0xFF003257),
    primaryContainer = Color(0xFF004A7D),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Slate80,
    onSecondary = Color(0xFF20333F),
    tertiary = Indigo80,
    background = Color(0xFF111318),
    surface = Color(0xFF111318),
    surfaceVariant = Color(0xFF42474E),
    onSurface = Color(0xFFE1E2E8),
    onSurfaceVariant = Color(0xFFC2C7CF)
)

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Slate40,
    onSecondary = Color.White,
    tertiary = Indigo40,
    background = Color(0xFFF8F9FD),
    surface = Color(0xFFF8F9FD),
    surfaceVariant = Color(0xFFE0E2EC),
    onSurface = Color(0xFF1A1C1F),
    onSurfaceVariant = Color(0xFF44474F)
)

@Composable
fun DSCRCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Keep a consistent visual identity across devices.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
