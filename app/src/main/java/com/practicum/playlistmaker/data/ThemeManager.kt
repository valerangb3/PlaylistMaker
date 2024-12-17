package com.practicum.playlistmaker.data

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class ThemeManager(
    private val app: Application,
    private val sharedPrefs: SharedPreferences
) {

    companion object {
        const val DARK_THEME_KEY = "key_for_dark_theme"
    }

    //Получаем тему устройства по умолчанию
    private fun getDeviceTheme(): Boolean = app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun getTheme(): Boolean = sharedPrefs.getBoolean(DARK_THEME_KEY, getDeviceTheme())

    fun saveTheme(theme: Boolean) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, theme)
            .apply()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkThemeEnabled)
    }
}