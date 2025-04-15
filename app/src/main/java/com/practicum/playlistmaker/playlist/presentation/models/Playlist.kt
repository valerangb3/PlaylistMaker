package com.practicum.playlistmaker.playlist.presentation.models

import java.io.Serializable

data class Playlist(
    var playlistId: Long = 0,
    val title: String,
    val description: String,
    val fileUri: String
) : Serializable
