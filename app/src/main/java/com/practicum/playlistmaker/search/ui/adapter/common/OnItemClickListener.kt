package com.practicum.playlistmaker.search.ui.adapter.common

import com.practicum.playlistmaker.search.domain.models.Track

fun interface OnItemClickListener {
    fun onItemClick(trackItem: Track)
}