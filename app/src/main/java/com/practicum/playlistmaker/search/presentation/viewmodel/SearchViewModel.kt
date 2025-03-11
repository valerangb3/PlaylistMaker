package com.practicum.playlistmaker.search.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.TracksHistoryInteractor
import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.search.presentation.state.TrackListState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val loadTracksUseCase: TracksInteractor,
    private val tracksHistoryInteractor: TracksHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    private var tracksState = MutableLiveData<TrackListState>()


    private fun makeRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            tracksState.postValue(TrackListState.Loading)
            viewModelScope.launch {
                loadTracksUseCase.searchTracks(newSearchText).collect { pair ->
                    val (foundTracks, httpStatus) = pair
                    when (httpStatus) {
                        500 -> {
                            tracksState.postValue(TrackListState.Error(
                                error = ErrorType.CONNECTION_ERROR
                            ))
                        }
                        400 -> {
                            tracksState.postValue(TrackListState.Error(
                                error = ErrorType.EMPTY_DATA
                            ))
                        }
                        200 -> {
                            if (foundTracks != null) {
                                tracksState.postValue(TrackListState.SearchContent(data = foundTracks))
                            } else {
                                tracksState.postValue(TrackListState.Error(
                                    error = ErrorType.EMPTY_DATA
                                ))
                            }
                        }
                    }

                }
            }
        }
    }

    fun repeatRequest() {
        searchJob?.cancel()
        makeRequest(latestSearchText ?: "")
    }
    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }
        latestSearchText = changedText

        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            makeRequest(changedText)
        }
    }

    fun getTracksState(): LiveData<TrackListState> = tracksState

    fun addToHistory(track: Track) {
        tracksHistoryInteractor.addToHistory(track)
    }
    fun showHistoryList() {
        latestSearchText = ""
        searchJob?.cancel()
        tracksState.value = TrackListState.HistoryContent(tracksHistoryInteractor.getHistory())
    }
    fun getHistoryList() = tracksHistoryInteractor.getHistory()
    fun saveHistory() {
        tracksHistoryInteractor.saveHistory(tracksHistoryInteractor.getHistory())
    }
    fun clearHistory() {
        tracksHistoryInteractor.remove()
    }
}