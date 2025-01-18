package com.practicum.playlistmaker.player.mapper

import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.search.domain.models.Track

object TrackInfoMapper {
    fun toTrackInfoMapper(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            country = track.country,
            artworkUrl512 = track.getCoverArtwork(),
        )
    }
}