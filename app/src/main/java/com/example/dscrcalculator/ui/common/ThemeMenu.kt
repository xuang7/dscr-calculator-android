package com.example.dscrcalculator.ui.common

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.dscrcalculator.R
import com.example.dscrcalculator.data.preferences.ThemePreference

@Composable
fun ThemeMenu() {
    val context = LocalContext.current
    var expanded by rememberSaveable { mutableStateOf(false) }
    val currentMode = resolveNightMode(context = context)
    val themeOptions = listOf(
        R.string.theme_system to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        R.string.theme_light to AppCompatDelegate.MODE_NIGHT_NO,
        R.string.theme_dark to AppCompatDelegate.MODE_NIGHT_YES
    )

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = stringResource(R.string.change_theme)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        themeOptions.forEach { (labelRes, mode) ->
            DropdownMenuItem(
                text = { Text(stringResource(labelRes)) },
                trailingIcon = {
                    if (currentMode == mode) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    }
                },
                onClick = {
                    expanded = false
                    if (currentMode != mode) {
                        ThemePreference.setNightMode(context, mode)
                        AppCompatDelegate.setDefaultNightMode(mode)
                    }
                }
            )
        }
    }
}

private fun resolveNightMode(context: Context): Int {
    val appCompatMode = AppCompatDelegate.getDefaultNightMode()
    return if (appCompatMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
        ThemePreference.getNightMode(context)
    } else {
        appCompatMode
    }
}
