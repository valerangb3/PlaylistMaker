package com.practicum.playlistmaker.media.presentation.state

sealed interface FavoriteState {
    data class EmptyContent(val textContent: String) : FavoriteState
}