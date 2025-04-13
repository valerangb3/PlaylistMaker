package com.practicum.playlistmaker.player.presentation.state

import com.practicum.playlistmaker.player.ui.models.PlaylistTrack

sealed interface PlaylistScreenState {
    object Loading: PlaylistScreenState
    data object PlaylistScreenEmptyContentState : PlaylistScreenState
    data class PlaylistScreenContentState(val data: List<PlaylistTrack>)  : PlaylistScreenState
    data class PlaylistContentState(val data: PlaylistTrack)  : PlaylistScreenState
}