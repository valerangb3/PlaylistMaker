package com.practicum.playlistmaker.medialibrary.data.db.converter

import com.practicum.playlistmaker.medialibrary.data.db.entity.PlaylistTrack
import com.practicum.playlistmaker.medialibrary.data.db.entity.TrackEntity
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track

class PlaylistConverter {

    private fun toListId(items: List<TrackEntity>): MutableList<Long> {
        return items.map {
            it.trackId
        }.toMutableList()
    }

    private fun toTrackList(items: List<TrackEntity>): MutableList<Track> {
        return items.map {
            Track(
                trackId = it.trackId,
                trackTime = it.trackTime,
                trackName = it.trackName,
                primaryGenreName = it.primaryGenreName,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                country = it.country,
                previewUrl = it.previewUrl,
                artworkUrl100 = it.artworkUrl100,
                artistName = it.artistName
            )
        }.toMutableList()
    }

    fun convert(item: PlaylistTrack): Playlist {
        return Playlist(
            id = item.playlist.playlistId,
            title = item.playlist.title,
            description = item.playlist.description,
            pathSrc = item.playlist.pathSrc,
            tracksId = toListId(item.trackList),
            tracks = toTrackList(item.trackList)
        )
    }

    fun convert(items: List<PlaylistTrack>) : List<Playlist> {
        return items.map {
            Playlist(
                id = it.playlist.playlistId,
                title = it.playlist.title,
                description = it.playlist.description,
                pathSrc = it.playlist.pathSrc,
                tracksId = toListId(it.trackList)
            )
        }
    }
}