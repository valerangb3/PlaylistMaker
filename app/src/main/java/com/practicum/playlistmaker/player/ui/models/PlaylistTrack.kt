package com.practicum.playlistmaker.player.ui.models

data class PlaylistTrack(
    val playlistId: Long,
    val title: String,
    var trackCount: Int,
    val posterPath: String,
    val playlistItemsId: MutableList<Long>,
    val trackItems: MutableList<Track>
)