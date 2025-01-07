package com.practicum.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val track: Track,
    private val playerUserCase: PlayerInteractor
): ViewModel() {

    private val trackInfo: TrackInfo

    private var screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    /*

   */

    init {
        trackInfo = toTrackInfoMapper(track)
        trackInfo.previewUrl?.let {
            playerUserCase.prepareTrack(
                url = trackInfo.previewUrl,
                events = object : PlayerInteractor.TrackHandler {
                    override fun onProgress(progress: Long) {
                        playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                    }

                    override fun onComplete() {
                        playStatusLiveData.value = getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                    }

                    override fun onPause() {
                        playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                    }

                    override fun onStart() {
                        playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = true)
                    }

                    override fun onLoad() {
                        screenStateLiveData.postValue(PlayerScreenState.Content(trackInfo))
                    }
                }
            )
        }
    }

    companion object {
        fun factory(trackItem: Track): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    PlayerViewModel(
                        track = trackItem,
                        playerUserCase = Creator.provideMediaPlayer()
                    )
                }
            }
        }
    }

    private fun toTrackInfoMapper(track: Track): TrackInfo {
        return TrackInfo(
            trackId = track.trackId,
            trackName = track.trackName,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl,
            primaryGenreName = track.primaryGenreName,
            collectionName = track.collectionName,
            artistName = track.artistName,
            releaseDate = track.releaseDate,
            country = track.country,
            artworkUrl512 = track.getCoverArtwork(),
        )
    }

    private fun getCurrentPlayStatus(): PlayStatus {
        return playStatusLiveData.value ?: PlayStatus(progress = 0L, isPlaying = false)
    }

    fun getScreenStateLiveData(): LiveData<PlayerScreenState> = screenStateLiveData
    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData

    fun playback() {
        playerUserCase.playback()
    }

    fun pause() {
        playerUserCase.pause()
    }

    override fun onCleared() {
        playerUserCase.release()
    }
}