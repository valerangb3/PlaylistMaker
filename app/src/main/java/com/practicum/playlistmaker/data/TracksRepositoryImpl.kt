package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TracksSearchRequest
import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import com.practicum.playlistmaker.domain.api.TracksRepository
import com.practicum.playlistmaker.domain.models.Resource
import com.practicum.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    companion object {
        const val ERROR = "Network error"
    }
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response is TracksSearchResponse) {
            val tracksList = response.results.filter {
                it.trackName.isNotEmpty() &&
                it.artistName.isNotEmpty() &&
                it.trackTimeMillis > 0
            }.map {
                Track(
                    trackId = it.trackId,
                    trackName = it.trackName,
                    artistName = it.artistName,
                    trackTime = it.getFormatTime(),
                    artworkUrl100 = it.artworkUrl100,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName,
                    country = it.country,
                    previewUrl = it.previewUrl
                )

            }
            Resource.Success(tracksList)
        } else {
            Resource.Error(ERROR)
        }
    }
}