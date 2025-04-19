package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.presentation.state.PlaylistDetailState
import com.practicum.playlistmaker.media.ui.models.PlaylistDetail
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track
import com.practicum.playlistmaker.media.ui.models.Track as UiTrack
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private var screenStateLiveData = MutableLiveData<PlaylistDetailState>(PlaylistDetailState.Idle)

    fun getScreenStateLiveData(): LiveData<PlaylistDetailState> = screenStateLiveData

    private fun getTotalDuration(tracks: List<Track>): Long {
        var duration: Long = 0
        tracks.forEach { track ->
            duration += track.getTimeMillis()
        }
        return duration
    }

    private fun map(tracks: List<Track>): List<UiTrack> {
        return tracks.map { track ->
            UiTrack(
                primaryGenreName = track.primaryGenreName ?: "",
                collectionName = track.collectionName ?: "",
                artworkUrl100 = track.artworkUrl100,
                artistName = track.artistName,
                releaseDate = track.releaseDate ?: "",
                previewUrl = track.previewUrl ?: "",
                trackName = track.trackName,
                trackTime = track.getTimeMillis(),
                trackId = track.trackId,
                country = track.country ?: ""
            )
        }
    }

    private fun convert(playlist: Playlist): PlaylistDetail {
        return PlaylistDetail(
            playlistId = playlist.id,
            description = playlist.description,
            title = playlist.title,
            poster = playlist.pathSrc,
            duration = getTotalDuration(playlist.tracks),
            trackList = map(playlist.tracks)
        )
    }

    fun getPlaylistDetail(playlistId: Long) {
        screenStateLiveData.postValue(PlaylistDetailState.Loading)
        viewModelScope.launch {
            playlistRepository.getPlaylistItem(playlistId = playlistId).collect { playlist ->
                screenStateLiveData.postValue(
                    PlaylistDetailState.PlaylistDetailContent(
                        data = convert(
                            playlist = playlist
                        )
                    )
                )
            }
        }
    }
}