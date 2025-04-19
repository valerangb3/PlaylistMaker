package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist as PlaylistMediaLibrary
import com.practicum.playlistmaker.media.presentation.state.PlaylistState
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private var isClickAllowed = true

    init {
        //getPlaylistAll()
    }

    private var items = mutableListOf<Playlist>()
    private var screenStateLiveData = MutableLiveData<PlaylistState>(PlaylistState.Loading)

    fun getScreenStateLiveData(): LiveData<PlaylistState> = screenStateLiveData
    fun addPlaylist(item: Playlist) {
        items.add(item)
        postData()
    }

    fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun map(items: List<PlaylistMediaLibrary>): List<Playlist> {
        return items.map {
            Playlist(
                id = it.id,
                title = it.title,
                filePath = it.pathSrc,
                count = it.tracksId.size
            )
        }
    }

    private fun postData() {
        screenStateLiveData.postValue(
            PlaylistState.PlaylistContent(
                data = items
            )
        )
    }

    fun getPlaylistAll() {
        viewModelScope.launch {
            playlistRepository.getPlaylistItems().collect { playlistItems ->
                if (playlistItems.isEmpty()) {
                    screenStateLiveData.postValue(PlaylistState.EmptyContent(R.string.playlist_empty))
                } else {
                    items = map(
                        items = playlistItems
                    ).toMutableList()
                    postData()
                }
            }
        }
    }
}