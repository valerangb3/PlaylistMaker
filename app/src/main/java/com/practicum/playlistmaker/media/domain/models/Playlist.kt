package com.practicum.playlistmaker.media.domain.models

data class Playlist(
    val title: String,
    val filePath: String,
    val count: Int = 0,
)