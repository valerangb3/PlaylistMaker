package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.favourites.data.db.repository.FavouriteRepositoryImpl
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository
import com.practicum.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.PlayerRepository
import com.practicum.playlistmaker.search.data.repository.TracksHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import com.practicum.playlistmaker.settings.data.repository.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            favouriteRepository = get()
        )
    }

    single<TracksHistoryRepository> {
        TracksHistoryRepositoryImpl(
            sharedPreferences = get(),
            gson = get(),
            favouriteRepository = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            app = androidApplication(),
            sharedPrefs = get()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(
            context = androidContext()
        )
    }

    factory <PlayerRepository> {
        PlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }

    single <FavouriteRepository> {
        FavouriteRepositoryImpl(
            appDatabase = get(),
            mapper = get()
        )
    }
}