package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.PlaylistState
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    init {
        getPlaylistAll()
    }

    private var screenStateLiveData = MutableLiveData<PlaylistState>(PlaylistState.EmptyContent(R.string.playlist_empty))

    fun getScreenStateLiveData(): LiveData<PlaylistState> = screenStateLiveData


    fun getPlaylistAll() {
        viewModelScope.launch {
            playlistRepository.getPlaylistItems().collect {

            }
        }
    }
}