package com.practicum.playlistmaker.medialibrary.data.db.mappers

import com.practicum.playlistmaker.medialibrary.data.db.entity.FavouriteTrackEntity
import com.practicum.playlistmaker.medialibrary.domain.favourite.models.Favourite

class FavouriteMap {

    interface MapToFavourite<T> {
        fun toFavourite(item: T): Favourite
    }

    fun <T> map(item: T, map: MapToFavourite<T>): Favourite {
        return map.toFavourite(item)
    }

    fun map(itemList: List<FavouriteTrackEntity>): List<Favourite> {
        return itemList.map { map(it) }
    }

    fun map(item: FavouriteTrackEntity): Favourite {
        return Favourite(
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
            previewUrl = item.previewUrl,
            timestamp = System.currentTimeMillis()
        )
    }
}