package com.practicum.playlistmaker.playlist.presentation.state

sealed interface PlaylistMakerState {
    data class Create(val playlistId: Long, val playlistName: String) : PlaylistMakerState
    data object Idle : PlaylistMakerState
}