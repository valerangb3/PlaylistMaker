package com.practicum.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.playlist.presentation.models.Playlist
import com.practicum.playlistmaker.playlist.presentation.state.PlaylistMakerState
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist as PlaylistMedia
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private var screenStateLiveData = MutableLiveData<PlaylistMakerState>(PlaylistMakerState.Idle)

    private fun map(item: Playlist): PlaylistMedia {
        return PlaylistMedia(
            title = item.title,
            description = item.description,
            pathSrc = item.fileUri,
        )
    }

    fun getScreenStateLiveData(): LiveData<PlaylistMakerState> = screenStateLiveData

    fun addPlaylist(item: Playlist) {
        viewModelScope.launch {
            val playlistId = playlistRepository.addPlaylist(map(item))
            screenStateLiveData.postValue(PlaylistMakerState.Create(
                playlistId = playlistId,
                playlistName = item.title
            ))
        }
    }


}