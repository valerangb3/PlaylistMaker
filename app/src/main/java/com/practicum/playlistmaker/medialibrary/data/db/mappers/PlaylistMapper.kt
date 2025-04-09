package com.practicum.playlistmaker.medialibrary.data.db.mappers

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist

class PlaylistMapper {
    fun map(item: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = item.id,
            title = item.title,
            description = item.description,
            tracks = item.tracks,
            pathSrc = item.pathSrc,
            trackCount = item.trackCount
        )
    }

    fun map(items: List<PlaylistEntity>): List<Playlist> {
        return items.map {
            Playlist(
                id = it.id,
                title = it.title,
                description = it.description,
                tracks = it.tracks,
                pathSrc = it.pathSrc,
                trackCount = it.trackCount
            )
        }
    }
}