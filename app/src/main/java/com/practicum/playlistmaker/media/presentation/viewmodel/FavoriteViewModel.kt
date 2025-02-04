package com.practicum.playlistmaker.media.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.FavoriteState

class FavoriteViewModel(
    application: Application
) : AndroidViewModel(application) {

    private var screenStateLiveData = MutableLiveData<FavoriteState>(FavoriteState.EmptyContent(application.getString(R.string.favorites_empty)))

    fun getScreenStateLiveData(): LiveData<FavoriteState> = screenStateLiveData

}