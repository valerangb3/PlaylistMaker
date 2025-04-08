package com.practicum.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.playlist.presentation.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist as PlaylistMedia
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {



    private fun map(item: Playlist): PlaylistMedia {
        return PlaylistMedia(
            title = item.title,
            description = item.description,
            pathSrc = item.fileUri
        )
    }

    fun addPlaylist(item: Playlist) {
        viewModelScope.launch {
            playlistRepository.addPlaylist(map(item))
        }
    }

}