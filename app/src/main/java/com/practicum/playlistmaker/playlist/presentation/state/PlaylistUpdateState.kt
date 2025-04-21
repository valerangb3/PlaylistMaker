package com.practicum.playlistmaker.playlist.presentation.state

import com.practicum.playlistmaker.playlist.presentation.models.Playlist

sealed interface PlaylistUpdateState {
    data object Idle: PlaylistUpdateState
    data object Loading: PlaylistUpdateState
    data object Update: PlaylistUpdateState
    data class PlaylistState(val playlist: Playlist): PlaylistUpdateState
}