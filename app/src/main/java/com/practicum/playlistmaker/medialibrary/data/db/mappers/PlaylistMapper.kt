package com.practicum.playlistmaker.medialibrary.data.db.mappers

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track

class PlaylistMapper {
    fun map(item: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = item.id,
            title = item.title,
            description = item.description,
            pathSrc = item.pathSrc,
        )
    }

    fun map(items: List<PlaylistEntity>): List<Playlist> {
        return items.map {
            Playlist(
                id = it.playlistId,
                title = it.title,
                description = it.description,
                pathSrc = it.pathSrc,
                tracksId = mutableListOf()
            )
        }
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            artworkUrl100 = track.artworkUrl100,
            primaryGenreName = track.primaryGenreName ?: "",
            collectionName = track.collectionName ?: "",
            previewUrl = track.previewUrl ?: "",
            artistName = track.artistName,
            releaseDate = track.releaseDate ?: "",
            trackName = track.trackName,
            trackTime = track.trackTime,
            timestamp = System.currentTimeMillis(),
            country = track.country ?: ""
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            releaseDate = trackEntity.releaseDate,
            trackName = trackEntity.trackName,
            trackTime = trackEntity.trackTime,
            country = trackEntity.country,
            artworkUrl100 = trackEntity.artworkUrl100,
            previewUrl = trackEntity.previewUrl,
            artistName = trackEntity.artistName,
            collectionName = trackEntity.collectionName,
            trackId = trackEntity.trackId,
            primaryGenreName = trackEntity.primaryGenreName
        )
    }
}