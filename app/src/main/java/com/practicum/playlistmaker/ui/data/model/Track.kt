package com.practicum.playlistmaker.ui.data.model

import java.io.Serializable

//TODO оставляю пока здесь - пригодится при полном переходе на compose
data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,

    //val collectionName: String?,
    //val releaseDate: String?,
    //val primaryGenreName: String?,
    //val country: String?,
    //val previewUrl: String?,
    //var inFavourite: Boolean
): Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}