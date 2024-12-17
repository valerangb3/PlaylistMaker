package com.practicum.playlistmaker.data.network

import com.practicum.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackSearch {

    @GET("/search?entity=song")
    fun getTrackList(@Query("term") searchText: String): Call<TracksSearchResponse>
}