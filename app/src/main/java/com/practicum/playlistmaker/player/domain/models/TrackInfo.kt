package com.practicum.playlistmaker.player.domain.models

import java.io.Serializable

data class TrackInfo(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl512: String,

    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
    var inFavourite: Boolean
) : Serializable