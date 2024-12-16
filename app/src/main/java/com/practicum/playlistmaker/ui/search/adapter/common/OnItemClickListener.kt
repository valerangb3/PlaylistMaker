package com.practicum.playlistmaker.ui.search.adapter.common

import com.practicum.playlistmaker.domain.models.Track

fun interface OnItemClickListener {
    fun onItemClick(trackItem: Track)
}