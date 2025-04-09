package com.practicum.playlistmaker.medialibrary.domain.playlist

import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    suspend fun addPlaylist(item: Playlist)
    suspend fun getPlaylistItems(): Flow<List<Playlist>>
}