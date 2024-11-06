package com.practicum.playlistmaker.track.adapter.common

import com.practicum.playlistmaker.data.Track

fun interface OnItemClickListener {
    fun onItemClick(trackItem: Track)
}