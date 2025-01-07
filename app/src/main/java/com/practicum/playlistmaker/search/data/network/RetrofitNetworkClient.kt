package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {

    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchTrackApi = retrofit.create(TrackSearch::class.java)

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