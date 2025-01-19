package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest

class RetrofitNetworkClient(
    private val searchTrackApi: TrackSearch
) : NetworkClient {

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) {
            try {
                val resp = searchTrackApi.getTrackList(dto.expression).execute()
                val body = resp.body() ?: Response()

                body.apply {
                    resultCode = resp.code()
                }
            } catch (ex: Exception) {
                return Response().apply { resultCode = 500 }
            }
        } else {
            Response().apply { resultCode = 404 }
        }
    }
}