package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val searchTrackApi: TrackSearch
) : NetworkClient {

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 }
        }
        return withContext(Dispatchers.IO) {
            try {
                val response = searchTrackApi.getTrackList(dto.expression)
                response.apply {
                    resultCode = 200
                }
            } catch (ex: Exception) {
                Response().apply { resultCode = 500 }
            }
        }
    }
}