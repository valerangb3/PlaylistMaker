package com.practicum.playlistmaker.player.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favourites.data.db.mappers.FavouriteMap
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository
import com.practicum.playlistmaker.favourites.domain.models.Favourite
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.mapper.TrackInfoMapper
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerUserCase: PlayerInteractor,
    private val favouriteRepository: FavouriteRepository,
    private val map: FavouriteMap
) : ViewModel() {

    private val trackInfo: TrackInfo by lazy {
        TrackInfoMapper.toTrackInfoMapper(track)
    }
    private var timerJob: Job? = null
    private var screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    //TODO завести новый стейт для избранного
    //private var inFavourite = MutableLiveData<>()

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
                        timerJob?.cancel()
                        playStatusLiveData.value =
                            getCurrentPlayStatus().copy(progress = 0L, isPlaying = false)
                    }

                    override fun onPause(isPlaying: Boolean) {
                        playStatusLiveData.value =
                            getCurrentPlayStatus().copy(isPlaying = isPlaying)
                    }

                    override suspend fun onStart(isPlaying: Boolean) {
                        playStatusLiveData.value = getCurrentPlayStatus().copy(
                            isPlaying = true
                        )
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

    fun addToFavourite(trackItem: TrackInfo) {
        viewModelScope.launch {
            val favourite = map.map(
                trackItem,
                object : FavouriteMap.MapToFavourite<TrackInfo> {
                    override fun toFavourite(item: TrackInfo): Favourite {
                        return Favourite(
                            trackId = item.trackId,
                            trackName = item.trackName,
                            artistName = item.artistName,
                            trackTime = item.trackTime,
                            artworkUrl100 = item.artworkUrl512,
                            collectionName = item.collectionName ?: "",
                            releaseDate = item.releaseDate ?: "",
                            primaryGenreName = item.primaryGenreName ?: "",
                            country = item.country ?: "",
                            previewUrl = item.previewUrl ?: ""
                        )
                    }
                })
            favouriteRepository.addToFavourite(
                item = favourite
            )
        }
    }

    fun playback() {
        timerJob = viewModelScope.launch {
            playerUserCase.playback()
        }
    }

    fun pause() {
        timerJob?.cancel()
        playerUserCase.pause()
    }

    override fun onCleared() {
        timerJob?.cancel()
        playerUserCase.release()
    }
}