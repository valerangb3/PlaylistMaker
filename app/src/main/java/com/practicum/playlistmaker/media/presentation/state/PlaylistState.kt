package com.practicum.playlistmaker.media.presentation.state

import androidx.annotation.StringRes
import com.practicum.playlistmaker.media.domain.models.Playlist

sealed interface PlaylistState {
    object Loading : PlaylistState
    data class PlaylistContent(val data : List<Playlist>) : PlaylistState
    data class EmptyContent(@StringRes val res: Int) : PlaylistState
}
