package com.practicum.playlistmaker.favourites.data.db.mappers

import com.practicum.playlistmaker.favourites.data.db.entity.FavouriteTrackEntity
import com.practicum.playlistmaker.favourites.domain.models.Favourite

class FavouriteMap {

    interface MapToFavourite<T> {
        fun toFavourite(item: T): Favourite
    }

    fun <T> map(item: T, map: MapToFavourite<T>): Favourite {
        return map.toFavourite(item)
    }

    fun map(item: Favourite): FavouriteTrackEntity {
        return FavouriteTrackEntity(
            trackId = item.trackId,
            trackName = item.trackName,
            artistName = item.artistName,
            trackTime = item.trackTime,
            artworkUrl100 = item.artworkUrl100,
            collectionName = item.collectionName,
            releaseDate = item.releaseDate,
            primaryGenreName = item.primaryGenreName,
            country = item.country,
            previewUrl = item.previewUrl
        )
    }
}