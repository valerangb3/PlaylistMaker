package com.practicum.playlistmaker.ui.data.state

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.data.model.ErrorType

//TODO оставляю пока здесь - пригодится при полном переходе на compose
sealed interface SearchState {
    object Loading : SearchState
    data class SearchContent(val data: List<Track>) : SearchState
    data class HistoryContent(val data: List<Track>) : SearchState
    data class Error(val error: ErrorType) : SearchState
}