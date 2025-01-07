package com.practicum.playlistmaker.settings.data.repository

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val app: Application,
    private val sharedPrefs: SharedPreferences
): SettingsRepository {
    companion object {
        const val DARK_THEME_KEY = "key_for_dark_theme"
    }

    //Получаем тему устройства по умолчанию
    private fun getDeviceTheme(): Boolean = app.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    private fun saveTheme(theme: Boolean) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME_KEY, theme)
            .apply()
    }

    override fun getTheme(): Boolean = sharedPrefs.getBoolean(DARK_THEME_KEY, getDeviceTheme())

    override fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        if (darkThemeEnabled != getTheme()) {
            saveTheme(darkThemeEnabled)
        }
    }
}