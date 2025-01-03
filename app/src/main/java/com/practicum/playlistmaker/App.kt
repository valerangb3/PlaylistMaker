package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.api.ThemeInteractor

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var themeUseCase: ThemeInteractor

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "key_for_dark_theme"
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        themeUseCase = Creator.provideThemeInteractor()
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }

    fun getSharedPreferences(): SharedPreferences = sharedPrefs
}