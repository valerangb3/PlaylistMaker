package com.practicum.playlistmaker.playlist.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.playlist.presentation.state.PlaylistUpdateState
import kotlinx.coroutines.launch
import com.practicum.playlistmaker.playlist.presentation.models.Playlist as PlaylistUi
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist

class PlaylistUpdateViewModel(
    private val repository: PlaylistRepository
) : PlaylistViewModel(playlistRepository = repository) {

    private var screenStateUpdateLiveData =
        MutableLiveData<PlaylistUpdateState>(PlaylistUpdateState.Idle)

    fun getScreenStateUpdateLiveData(): LiveData<PlaylistUpdateState> = screenStateUpdateLiveData

    private fun map(item: Playlist): PlaylistUi {
        return PlaylistUi(
            playlistId = item.id,
            description = item.description,
            title = item.title,
            fileUri = item.pathSrc
        )
    }

    private fun map(item: PlaylistUi): Playlist {
        return Playlist(
            description = item.description,
            title = item.title,
            pathSrc = item.fileUri
        )
    }

    fun updatePlaylist(playlistId: Long, newData: PlaylistUi) {
        viewModelScope.launch {
            repository.updatePlaylistItem(playlistId = playlistId, newData = map(item = newData))
            screenStateUpdateLiveData.postValue(
                PlaylistUpdateState.Update
            )
        }
    }

    fun getPlaylist(playlistId: Long) {
        viewModelScope.launch {
            repository.getPlaylistItem(playlistId = playlistId).collect { playlist ->
                screenStateUpdateLiveData.postValue(
                    PlaylistUpdateState.PlaylistState(
                        playlist = map(
                            playlist
                        )
                    )
                )
            }
        }
    }


}