package com.practicum.playlistmaker.media.presentation.state

import androidx.annotation.StringRes

sealed interface FavoriteState {
    data class EmptyContent(@StringRes val res: Int) : FavoriteState
}