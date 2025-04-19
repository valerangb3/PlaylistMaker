package com.practicum.playlistmaker.media.ui.models

import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,

    val trackTime: Long,
    val artworkUrl100: String,

    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun getFormatTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
}
