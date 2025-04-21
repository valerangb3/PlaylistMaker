package com.practicum.playlistmaker.medialibrary.domain.playlist

import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun getPlaylistItems(): Flow<List<Playlist>>
    suspend fun getPlaylistItem(playlistId: Long): Flow<Playlist>
    suspend fun updatePlaylistItem(playlistId: Long, newData: Playlist)

    suspend fun addPlaylist(item: Playlist): Long
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Long

    suspend fun deleteTrack(playlistId: Long, trackId: Long)
    suspend fun deletePlaylist(playlistId: Long)
}