package com.practicum.playlistmaker.medialibrary.domain.playlist.models


data class Playlist(
    val id: Long = 0,
    val title: String,
    val description: String,
    var pathSrc: String = "",

    val tracksId: MutableList<Long> = mutableListOf(),
    val tracks: MutableList<Track> = mutableListOf()
)
