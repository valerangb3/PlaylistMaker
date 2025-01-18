package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.data.dataModule
import com.practicum.playlistmaker.di.domain.interactorModule
import com.practicum.playlistmaker.di.domain.repositoryModule
import com.practicum.playlistmaker.di.viewmodel.viewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val themeUseCase: SettingsInteractor by inject()

    companion object {
        const val APP_PREFERENCES = "app_preferences"
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                dataModule,
                repositoryModule,
                interactorModule,
                viewModelModule
            )
        }
        themeUseCase.switchTheme(themeUseCase.getTheme())
    }
}