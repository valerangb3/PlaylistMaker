package com.practicum.playlistmaker.media.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.PlaylistState

class PlaylistViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var screenStateLiveData = MutableLiveData<PlaylistState>(PlaylistState.EmptyContent(application.getString(R.string.playlist_empty)))

    fun getScreenStateLiveData(): LiveData<PlaylistState> = screenStateLiveData

}