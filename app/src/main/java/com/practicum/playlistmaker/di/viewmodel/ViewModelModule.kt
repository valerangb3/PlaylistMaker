package com.practicum.playlistmaker.di.viewmodel

import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel<PlayerViewModel> { (track: Track) ->
        PlayerViewModel(
            track = track,
            playerUserCase = get(),
            favouriteRepository = get(),
            map = get()
        )
    }

    viewModel<SearchViewModel> {
        SearchViewModel(
            loadTracksUseCase = get(),
            tracksHistoryInteractor = get(),
        )
    }

    viewModel<SettingsViewModel> {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel<FavoriteViewModel> {
        FavoriteViewModel()
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel()
    }
}