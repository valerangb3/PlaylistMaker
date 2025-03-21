package com.practicum.playlistmaker.media.ui.adapter.common

import com.practicum.playlistmaker.media.domain.models.Favourite

fun interface OnItemClickListener {
    fun onItemClick(trackItem: Favourite)
}