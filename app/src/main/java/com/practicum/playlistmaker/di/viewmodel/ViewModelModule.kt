package com.practicum.playlistmaker.di.viewmodel

import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistDetailViewModel
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.playlist.presentation.viewmodel.PlaylistViewModel as PlaylistMakerViewModel
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<PlaylistMakerViewModel> {
        PlaylistMakerViewModel(playlistRepository = get())
    }

    viewModel<PlayerViewModel> { (track: TrackInfo) ->
        PlayerViewModel(
            trackInfo = track,
            playerUserCase = get(),
            favouriteRepository = get(),
            playlistRepository = get(),
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
        FavoriteViewModel(
            favouriteRepository = get()
        )
    }

    viewModel<PlaylistViewModel> {
        PlaylistViewModel(playlistRepository = get())
    }

    viewModel<PlaylistDetailViewModel> { (playlistId: Long) ->
        PlaylistDetailViewModel(playlistId = playlistId, playlistRepository = get(), share = get())
    }
}