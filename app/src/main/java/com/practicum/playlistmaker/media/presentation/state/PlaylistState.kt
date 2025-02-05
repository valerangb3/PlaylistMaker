package com.practicum.playlistmaker.media.presentation.state

sealed interface PlaylistState {
    data class EmptyContent(val textContent: String) : PlaylistState
}
