package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

object Creator {

    private lateinit var application: Application

    fun initApplication(application: App) {
        Creator.application = application
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(
            networkClient = RetrofitNetworkClient()
        )
    }

    private fun getTrackHistoryRepository(): TracksHistoryRepository {
        return TracksHistoryRepositoryImpl(
            sharedPreferences = application.getSharedPreferences(App.APP_PREFERENCES, MODE_PRIVATE)
        )
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            searchRepository = getTracksRepository()
        )
    }

    fun provideTrackHistoryInteractor(): TracksHistoryInteractor {
        return TracksHistoryInteractorImpl(
            tracksHistoryRepository = getTrackHistoryRepository()
        )
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(
                app = application,
                sharedPrefs = application.getSharedPreferences(App.APP_PREFERENCES, MODE_PRIVATE)
            )
        )
    }

    fun provideMediaPlayer(): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl(MediaPlayer()))
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(
            externalNavigator = ExternalNavigatorImpl(
                context = application
            )
        )
    }
}