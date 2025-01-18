package com.practicum.playlistmaker.search.presentation.state

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.ErrorType

sealed interface TrackListState {
    object Loading : TrackListState
    data class SearchContent(val data: List<Track>) : TrackListState
    data class HistoryContent(val data: List<Track>) : TrackListState
    data class Error(val error: ErrorType) : TrackListState
}