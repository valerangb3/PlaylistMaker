package com.practicum.playlistmaker.player.presentation.viewmodel

import android.util.Log
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
import com.practicum.playlistmaker.player.service.IPlayer
import com.practicum.playlistmaker.player.ui.models.PlaylistTrack
import com.practicum.playlistmaker.player.ui.models.Track
import com.practicum.playlistmaker.medialibrary.domain.playlist.models.Track as MediaTrack
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val trackInfo: TrackInfo,
    private val playerUserCase: PlayerInteractor,
    private val favouriteRepository: FavouriteRepository,
    private val playlistRepository: PlaylistRepository,
    private val map: FavouriteMap
) : ViewModel() {

    private var playerControl: IPlayer? = null

    private var screenPlaylistStateLiveData =
        MutableLiveData<PlaylistScreenState>(PlaylistScreenState.Loading)

    private val playStatusLiveData = MutableLiveData<PlayStatus>()
    private var inFavouriteLiveData = MutableLiveData<Boolean>()

    private var toastState = MutableLiveData<ToastState>(ToastState.None)

    init {
        inFavouriteLiveData.postValue(trackInfo.inFavourite)
        checkFavourite(trackInfo.trackId)
    }

    fun setPlayerControl(player: IPlayer?) {
        playerControl = player

        viewModelScope.launch {
            playerControl?.getPlayerStatus()?.collect {
                playStatusLiveData.postValue(it)
            }
        }
    }

    fun getScreenPlaylistStateLiveData(): LiveData<PlaylistScreenState> =
        screenPlaylistStateLiveData

    fun observeToastState(): LiveData<ToastState> = toastState

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
        viewModelScope.launch {
            playerControl?.playback()
        }
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

    fun showNotification() {
        if (playStatusLiveData.value?.isPlaying == true) {
            playerControl?.showNotification()
        }
    }

    fun hideNotification() {
        playerControl?.hideNotification()
    }

    override fun onCleared() {
        playerUserCase.release()
        removePlayerControl()
    }

    fun removePlayerControl() {
        playerControl = null
    }
}