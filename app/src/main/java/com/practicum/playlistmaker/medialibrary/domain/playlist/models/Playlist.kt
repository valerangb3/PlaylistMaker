package com.practicum.playlistmaker.medialibrary.domain.playlist.models

data class Playlist(
    val id: Long = 0,
    val title: String,
    val description: String,
    var pathSrc: String = "",
    val tracks: String = "",
    val trackCount: Int = 0
)
