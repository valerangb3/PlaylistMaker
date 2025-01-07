package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class App : Application() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var themeUseCase: SettingsInteractor

    companion object {
        const val APP_PREFERENCES = "app_preferences"
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        sharedPrefs = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        themeUseCase = Creator.provideSettingsInteractor()
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }

    fun getSharedPreferences(): SharedPreferences = sharedPrefs
}