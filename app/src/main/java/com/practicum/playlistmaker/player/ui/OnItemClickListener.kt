package com.practicum.playlistmaker.player.ui

import com.practicum.playlistmaker.player.ui.models.PlaylistTrack

fun interface OnItemClickListener {
    fun onItemClick(playlistItem: PlaylistTrack)
}