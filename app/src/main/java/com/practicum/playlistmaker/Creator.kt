package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.data.ThemeManager
import com.practicum.playlistmaker.data.TracksHistoryManager
import com.practicum.playlistmaker.data.TracksRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.domain.api.ThemeInteractor
import com.practicum.playlistmaker.domain.api.TracksHistoryInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.impl.ThemeInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        this.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient()
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            repository = getTracksRepository()
        )
    }

    fun provideTrackHistoryInteractor(): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(tracksHistoryManager = TracksHistoryManager(
            sharedPreferences = (application as App).getSharedPreferences()
        ))
    }

    fun provideThemeInteractor(): ThemeInteractor {
        return ThemeInteractorImpl(
            ThemeManager(
                app = application,
                sharedPrefs = (application as App).getSharedPreferences()
            )
        )
    }
}