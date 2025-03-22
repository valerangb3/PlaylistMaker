package com.practicum.playlistmaker.media.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository
import com.practicum.playlistmaker.favourites.domain.models.Favourite as FavouriteExt
import com.practicum.playlistmaker.media.domain.models.Favourite
import com.practicum.playlistmaker.media.presentation.state.FavoriteState
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val favouriteRepository: FavouriteRepository,
) : ViewModel() {


    init {
        getFavouriteList()
    }

    private var screenStateLiveData = MutableLiveData<FavoriteState>(FavoriteState.Loading)

    fun getScreenStateLiveData(): LiveData<FavoriteState> = screenStateLiveData

    private fun map(items: List<FavouriteExt>): List<Favourite> {
        return items.map { favouriteExt ->
            Favourite(
                trackId = favouriteExt.trackId,
                collectionName = favouriteExt.collectionName,
                country = favouriteExt.country,
                artistName = favouriteExt.artistName,
                releaseDate = favouriteExt.releaseDate,
                trackTime = favouriteExt.trackTime,
                trackName = favouriteExt.trackName,
                primaryGenreName = favouriteExt.primaryGenreName,
                artworkUrl100 = favouriteExt.artworkUrl100,
                previewUrl = favouriteExt.previewUrl
            )
        }
    }

    fun getFavouriteList() {
        viewModelScope.launch {
            favouriteRepository.getFavouriteItems().collect { favouriteList ->
                if (favouriteList.isEmpty()) {
                    screenStateLiveData.postValue(FavoriteState.EmptyContent(R.string.favorites_empty))
                } else {
                    screenStateLiveData.postValue(FavoriteState.FavouriteContent(map(favouriteList)))
                }
            }
        }
    }
}