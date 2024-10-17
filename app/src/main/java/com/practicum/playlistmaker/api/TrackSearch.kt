package com.practicum.playlistmaker.api

import com.practicum.playlistmaker.data.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrackSearch {

    @GET("/search?entity=song")
    fun getTrackList(@Query("term") searchText: String): Call<TrackResponse>
}