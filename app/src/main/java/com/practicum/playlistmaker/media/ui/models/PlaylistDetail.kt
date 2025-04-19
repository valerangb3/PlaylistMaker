package com.practicum.playlistmaker.media.ui.models

data class PlaylistDetail(
    val playlistId: Long,
    val poster: String,
    val title: String,
    val description: String,
    val duration: Long,
    val trackList: List<Track>
)