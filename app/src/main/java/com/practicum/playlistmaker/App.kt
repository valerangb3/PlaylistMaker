package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class App : Application() {

    private lateinit var themeUseCase: SettingsInteractor

    companion object {
        const val APP_PREFERENCES = "app_preferences"
    }

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        themeUseCase = Creator.provideSettingsInteractor()
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }
}