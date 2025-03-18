package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TracksInteractor
import com.practicum.playlistmaker.search.domain.models.Resource
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(
    private val searchRepository: TracksRepository
): TracksInteractor {

    override fun searchTracks(expression: String) : Flow<Pair<List<Track>?, Int>> {
        return searchRepository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, result.status)
                }
                is Resource.ClientError -> {
                    Pair(null, result.status)
                }
                is Resource.ServerError -> {
                    Pair(null, result.status)
                }
            }
        }
    }
}