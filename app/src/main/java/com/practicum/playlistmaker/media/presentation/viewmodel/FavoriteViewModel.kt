package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.FavoriteState

class FavoriteViewModel : ViewModel() {

    private var screenStateLiveData = MutableLiveData<FavoriteState>(FavoriteState.EmptyContent(R.string.favorites_empty))

    fun getScreenStateLiveData(): LiveData<FavoriteState> = screenStateLiveData

}