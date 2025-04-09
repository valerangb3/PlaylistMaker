package com.practicum.playlistmaker.playlist.presentation.state

sealed interface PlaylistMakerState {
    data class Create(val playlistName: String) : PlaylistMakerState
    data object Idle : PlaylistMakerState
}