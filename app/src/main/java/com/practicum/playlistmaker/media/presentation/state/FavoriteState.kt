package com.practicum.playlistmaker.media.presentation.state

import androidx.annotation.StringRes
import com.practicum.playlistmaker.media.domain.models.Favourite

sealed interface FavoriteState {
    object Loading : FavoriteState
    data class EmptyContent(@StringRes val res: Int) : FavoriteState
    data class FavouriteContent(val data : List<Favourite>) : FavoriteState
}