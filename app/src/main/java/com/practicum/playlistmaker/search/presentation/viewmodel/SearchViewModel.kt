package com.practicum.playlistmaker.search.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.search.presentation.state.TrackListState

class SearchViewModel(
    private val loadTracksUseCase: TracksInteractor,
    private val tracksHistoryInteractor: TracksHistoryInteractor
) : ViewModel() {

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L

        fun factory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    SearchViewModel(
                        loadTracksUseCase = Creator.provideTracksInteractor(),
                        tracksHistoryInteractor = Creator.provideTrackHistoryInteractor()
                    )
                }
            }
        }
    }

    private var latestSearchText: String? = null

    private val handler = Handler(Looper.getMainLooper())

    private var tracksState = MutableLiveData<TrackListState>()

    private fun removeCallbacksAndMessages() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun makeRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            tracksState.postValue(TrackListState.Loading)
            loadTracksUseCase.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: Resource<List<Track>>) {
                    when (foundTracks) {
                        is Resource.ServerError -> {
                            tracksState.postValue(TrackListState.Error(ErrorType.CONNECTION_ERROR))
                        }
                        is Resource.ClientError -> {
                            tracksState.postValue(TrackListState.Error(ErrorType.EMPTY_DATA))
                        }
                        is Resource.Success -> {
                            tracksState.postValue(TrackListState.SearchContent(data = foundTracks.data))
                        }
                    }
                }
            })
        }
    }

    fun repeatRequest() {
        removeCallbacksAndMessages()
        makeRequest(latestSearchText ?: "")
    }
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText

        removeCallbacksAndMessages()
        val searchRunnable = Runnable { makeRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun getTracksState(): LiveData<TrackListState> = tracksState

    fun addToHistory(track: Track) {
        tracksHistoryInteractor.addToHistory(track)
    }
    fun showHistoryList() {
        latestSearchText = ""
        removeCallbacksAndMessages()
        tracksState.value = TrackListState.HistoryContent(tracksHistoryInteractor.getHistory())
    }
    fun getHistoryList() = tracksHistoryInteractor.getHistory()
    fun saveHistory() {
        tracksHistoryInteractor.saveHistory(tracksHistoryInteractor.getHistory())
    }
    fun clearHistory() {
        tracksHistoryInteractor.remove()
    }

    override fun onCleared() {
        super.onCleared()
        removeCallbacksAndMessages()
    }
}