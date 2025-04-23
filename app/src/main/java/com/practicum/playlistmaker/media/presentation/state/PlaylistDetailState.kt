package com.practicum.playlistmaker.media.presentation.state

import com.practicum.playlistmaker.media.ui.models.PlaylistDetail

sealed interface PlaylistDetailState {
    object Idle: PlaylistDetailState
    object Loading: PlaylistDetailState
    data class PlaylistDetailContent(val data: PlaylistDetail): PlaylistDetailState
}