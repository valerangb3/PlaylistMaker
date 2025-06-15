package com.practicum.playlistmaker.di.domain

import com.practicum.playlistmaker.network.domain.CheckNetworkUseCase
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(
            searchRepository = get()
        )
    }

    single<TracksHistoryInteractor> {
        TracksHistoryInteractorImpl(
            tracksHistoryRepository = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(
            settingsRepository = get()
        )
    }

    single<SharingInteractor> {
        SharingInteractorImpl(
            externalNavigator = get()
        )
    }

    factory <PlayerInteractor> {
        PlayerInteractorImpl(
            playerRepository = get()
        )
    }


    single <CheckNetworkUseCase> {
        CheckNetworkUseCase(get())
    }
}