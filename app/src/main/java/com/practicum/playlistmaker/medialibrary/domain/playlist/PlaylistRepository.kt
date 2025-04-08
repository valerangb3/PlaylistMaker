package com.practicum.playlistmaker.medialibrary.domain.playlist

import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist

interface PlaylistRepository {

    suspend fun addPlaylist(item: Playlist)
}