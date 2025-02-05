package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.PlaylistState

class PlaylistViewModel : ViewModel() {

    private var screenStateLiveData = MutableLiveData<PlaylistState>(PlaylistState.EmptyContent(R.string.playlist_empty))

    fun getScreenStateLiveData(): LiveData<PlaylistState> = screenStateLiveData

}