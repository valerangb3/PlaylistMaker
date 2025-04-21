package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.media.presentation.state.PlaylistDetailState
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.media.ui.models.PlaylistDetail
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.utils.getWordForm
import kotlinx.coroutines.delay
import com.practicum.playlistmaker.media.ui.models.Track as UiTrack
import kotlinx.coroutines.launch

class PlaylistDetailViewModel(
    private val playlistId: Long,
    private val playlistRepository: PlaylistRepository,
    private val share: SharingInteractor
) : ViewModel() {

    private var screenStateLiveData = MutableLiveData<PlaylistDetailState>(PlaylistDetailState.Idle)
    private var isDeleteTrack = MutableLiveData(false)
    private var isDeletePlaylist = MutableLiveData(false)

    private var isClickAllowed = true

    var playlistTitle: String = ""
        private set
    private var playlistDescription: String = ""

    init {
        getPlaylistDetail()
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

    fun sharingPlaylist(tracks: List<UiTrack>) {
        val result = buildString {
            appendLine(playlistTitle)
            if (playlistDescription.isNotEmpty()) {
                appendLine(playlistDescription)
            }
            appendLine(getWordForm(tracks.size))
            tracks.forEachIndexed { index, track ->
                appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${track.getFormatTime()})")
            }
        }
        share.sharePlaylist(content = result)
    }

    fun getScreenStateLiveData(): LiveData<PlaylistDetailState> = screenStateLiveData
    fun isDeleteTrackLiveData(): LiveData<Boolean> = isDeleteTrack
    fun isDeletePlaylistLiveData(): LiveData<Boolean> = isDeletePlaylist

    fun deleteTrack(trackId: Long) {
        viewModelScope.launch {
            playlistRepository.deleteTrack(playlistId = playlistId, trackId = trackId)
            isDeleteTrack.postValue(true)
        }
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId = playlistId)
            isDeletePlaylist.postValue(true)
        }
    }

    fun getTotalDuration(tracks: List<UiTrack>): Long {
        var duration: Long = 0
        tracks.forEach { track ->
            duration += track.trackTime
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
        val uiTracks = map(playlist.tracks)
        return PlaylistDetail(
            playlistId = playlist.id,
            description = playlist.description,
            title = playlist.title,
            poster = playlist.pathSrc,
            duration = getTotalDuration(uiTracks),
            trackList = uiTracks
        )
    }

    fun getPlaylistDetail() {
        screenStateLiveData.postValue(PlaylistDetailState.Loading)
        viewModelScope.launch {
            playlistRepository.getPlaylistItem(playlistId = playlistId).collect { playlist ->
                playlistTitle = playlist.title
                playlistDescription = playlist.description
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