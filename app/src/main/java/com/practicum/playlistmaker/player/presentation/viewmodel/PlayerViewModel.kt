package com.practicum.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.medialibrary.data.db.mappers.FavouriteMap
import com.practicum.playlistmaker.medialibrary.domain.favourite.FavouriteRepository
import com.practicum.playlistmaker.medialibrary.domain.favourite.models.Favourite
import com.practicum.playlistmaker.medialibrary.domain.playlist.PlaylistRepository
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Playlist
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.models.PlayStatus
import com.practicum.playlistmaker.player.domain.models.TrackInfo
import com.practicum.playlistmaker.player.presentation.state.PlayerScreenState
import com.practicum.playlistmaker.player.presentation.state.PlaylistScreenState
import com.practicum.playlistmaker.player.presentation.state.ToastState
import com.practicum.playlistmaker.player.ui.models.PlaylistTrack
import com.practicum.playlistmaker.player.ui.models.Track
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track as MediaTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackInfo: TrackInfo,
    private val playerUserCase: PlayerInteractor,
    private val favouriteRepository: FavouriteRepository,
    private val playlistRepository: PlaylistRepository,
    private val map: FavouriteMap
) : ViewModel() {

    private var timerJob: Job? = null

    private var screenStateLiveData = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    private var screenPlaylistStateLiveData =
        MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Loading)

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private var inFavouriteLiveData = MutableLiveData<Boolean>()

    private var inPlaylist = MutableLiveData<Triple<String, Boolean, Boolean>>()
    private var toastState = MutableLiveData<ToastState>(ToastState.None)

    init {
        val previewUrl = trackInfo.previewUrl

        inFavouriteLiveData.postValue(trackInfo.inFavourite)
        checkFavourite(trackInfo.trackId)

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
    fun getScreenPlaylistStateLiveData(): LiveData<PlaylistScreenState> =
        screenPlaylistStateLiveData

    fun observeToastState(): LiveData<ToastState> = toastState

    fun trackInPlaylist(): LiveData<Triple<String, Boolean, Boolean>> = inPlaylist

    fun getPlayStatusLiveData(): LiveData<PlayStatus> = playStatusLiveData
    fun inFavouriteLiveData(): LiveData<Boolean> = inFavouriteLiveData


    private fun checkFavourite(trackId: Long) {
        viewModelScope.launch {
            favouriteRepository.getFavourite(trackId)
                .catch {
                    inFavouriteLiveData.postValue(false)
                }
                .collect {
                    inFavouriteLiveData.postValue(true)
                }
        }
    }

    private fun convertToFavourite(trackItem: TrackInfo): Favourite {
        return map.map(
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
    }

    private suspend fun addToFavourite(trackItem: TrackInfo) {
        favouriteRepository.addToFavourite(
            item = convertToFavourite(trackItem)
        )
    }

    private suspend fun removeFromFavourite(trackItem: TrackInfo) {
        favouriteRepository.deleteFromFavourite(
            item = convertToFavourite(trackItem)
        )
    }

    private fun convertTrack(track: Track): MediaTrack {
        return MediaTrack(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl512,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    private fun convertPlaylist(items: List<Playlist>): List<PlaylistTrack> {
        return items.map {
            PlaylistTrack(
                playlistId = it.id,
                title = it.title,
                trackCount = it.tracksId.size,
                posterPath = it.pathSrc,
                playlistItemsId = it.tracksId.toMutableList(),
                trackItems = it.tracks.map { track ->
                    Track(
                        trackId = track.trackId,
                        trackName = track.trackName,
                        artistName = track.artistName,
                        trackTime = track.trackTime,
                        artworkUrl512 = track.artworkUrl100,
                        collectionName = track.collectionName ?: "",
                        releaseDate = track.releaseDate ?: "",
                        primaryGenreName = track.primaryGenreName ?: "",
                        country = track.country ?: "",
                        previewUrl = track.previewUrl ?: ""
                    )
                }.toMutableList()
            )
        }
    }

    fun favouriteHandler(trackItem: TrackInfo) {
        viewModelScope.launch {
            if (trackItem.inFavourite) {
                removeFromFavourite(trackItem)
                inFavouriteLiveData.postValue(false)
            } else {
                addToFavourite(trackItem)
                inFavouriteLiveData.postValue(true)
            }
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

    fun addToPlayList(playlist: PlaylistTrack, track: Track) {
        viewModelScope.launch {
            if (!playlist.playlistItemsId.contains(track.trackId)) {
                playlistRepository.addTrackToPlaylist(
                    playlistId = playlist.playlistId,
                    track = convertTrack(track)
                )
                toastState.postValue(ToastState.Show(playlist.title, true))
                playlist.playlistItemsId.add(track.trackId)
                playlist.trackCount = playlist.playlistItemsId.size
                playlist.trackItems.add(track)

                screenPlaylistStateLiveData.postValue(
                    PlaylistScreenState.PlaylistContentState(
                        data = playlist
                    )
                )
            } else {
                toastState.postValue(ToastState.Show(playlist.title, false))
            }
        }
    }

    fun toastWasShown() {
        toastState.value = ToastState.None
    }

    fun getPlaylistItems() {
        screenPlaylistStateLiveData.postValue(
            PlaylistScreenState.Loading
        )
        viewModelScope.launch {
            playlistRepository.getPlaylistItems().collect { playlistItems ->
                if (playlistItems.isNotEmpty()) {
                    screenPlaylistStateLiveData.postValue(
                        PlaylistScreenState.PlaylistScreenContentState(
                            data = convertPlaylist(playlistItems)
                        )
                    )
                } else {
                    screenPlaylistStateLiveData.postValue(
                        PlaylistScreenState.PlaylistScreenEmptyContentState
                    )
                }
            }
        }
    }

    override fun onCleared() {
        timerJob?.cancel()
        playerUserCase.release()
    }
}