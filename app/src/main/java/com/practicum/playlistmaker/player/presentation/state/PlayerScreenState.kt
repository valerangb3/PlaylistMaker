package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.domain.models.TrackInfo

sealed interface PlayerScreenState {
    object Loading : PlayerScreenState
    class Content(
        val trackInfo: TrackInfo
    ) : PlayerScreenState
}