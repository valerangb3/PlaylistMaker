package com.practicum.playlistmaker.medialibrary.domain.playlist.models

import java.time.Duration

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,

    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
) {
    fun getTimeMillis(): Long {
        val duration = Duration.parse("PT${trackTime.replace(":", "M")}S")
        return duration.toMillis()
    }
}