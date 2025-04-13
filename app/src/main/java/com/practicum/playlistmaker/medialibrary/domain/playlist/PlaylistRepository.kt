package com.practicum.playlistmaker.medialibrary.domain.playlist

import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(item: Playlist): Long
    suspend fun getPlaylistItems(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Long
}