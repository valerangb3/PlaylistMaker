package com.practicum.playlistmaker.media.presentation.state

import androidx.annotation.StringRes

sealed interface PlaylistState {
    data class EmptyContent(@StringRes val res: Int) : PlaylistState
}
