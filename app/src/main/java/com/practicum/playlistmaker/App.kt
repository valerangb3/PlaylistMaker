package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.contains(DARK_THEME_KEY)
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_KEY, isDarkTheme()))
    }

    //Получаем тему устройства по умолчанию
    private fun getDeviceTheme(): Boolean = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun isDarkTheme(): Boolean = sharedPrefs.getBoolean(DARK_THEME_KEY, getDeviceTheme())

    fun saveTheme() {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, darkTheme)
            .apply()
    }

    fun getSharedPreferences(): SharedPreferences = sharedPrefs

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "key_for_dark_theme"
    }
}