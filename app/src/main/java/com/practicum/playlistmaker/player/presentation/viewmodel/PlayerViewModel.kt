package com.practicum.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track

class PlayerViewModel(
    private val track: Track,
    private val playerUserCase: PlayerInteractor
): ViewModel() {

    private val trackInfo: TrackInfo by lazy {
        TrackInfoMapper.toTrackInfoMapper(track)
    }

    private var screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()

    init {
        val previewUrl = trackInfo.previewUrl
        previewUrl?.let {
            playerUserCase.prepareTrack(
                url = previewUrl,
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