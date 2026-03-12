package com.example.dscrcalculator.ui.common

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
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
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.example.dscrcalculator.R
import java.util.Locale

@Composable
fun LanguageMenu() {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val appLanguageTags = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    val currentLanguage = when {
        appLanguageTags.contains("es") -> "es"
        appLanguageTags.contains("zh") -> "zh"
        appLanguageTags.contains("en") -> "en"
        else -> Locale.getDefault().language
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Default.Language,
            contentDescription = stringResource(R.string.change_language)
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.language_english)) },
            trailingIcon = {
                if (currentLanguage == "en") {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            },
            onClick = {
                expanded = false
                if (currentLanguage != "en") {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                }
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.language_chinese_simplified)) },
            trailingIcon = {
                if (currentLanguage == "zh") {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            },
            onClick = {
                expanded = false
                if (currentLanguage != "zh") {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("zh"))
                }
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.language_spanish)) },
            trailingIcon = {
                if (currentLanguage == "es") {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            },
            onClick = {
                expanded = false
                if (currentLanguage != "es") {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("es"))
                }
            }
        )
    }
}
