package com.practicum.playlistmaker.media.ui.adapter.common

import com.practicum.playlistmaker.media.domain.models.Favourite

fun interface OnFavouriteItemClickListener {
    fun onItemClick(trackItem: Favourite)
}