package com.practicum.playlistmaker.medialibrary.data.db.converter

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrack
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist

class PlaylistConverter {

    private fun toListId(items: List<TrackEntity>): MutableList<Long> {
        return items.map {
            it.trackId
        }.toMutableList()
    }

    fun convert(item: PlaylistTrack): Playlist {
        return Playlist(
            id = item.playlist.playlistId,
            title = item.playlist.title,
            description = item.playlist.description,
            pathSrc = item.playlist.pathSrc,
            tracksId = toListId(item.trackList)
        )
    }

    fun convert(items: List<PlaylistTrack>) : List<Playlist> {
        return items.map {
            convert(it)
        }
    }
}