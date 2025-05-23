package com.practicum.playlistmaker.di.data

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.medialibrary.data.db.AppDatabase
import com.practicum.playlistmaker.medialibrary.data.db.converter.PlaylistConverter
import com.practicum.playlistmaker.medialibrary.data.db.mappers.FavouriteMap
import com.practicum.playlistmaker.medialibrary.data.db.mappers.PlaylistMapper
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient.Companion.BASE_URL
import com.practicum.playlistmaker.search.data.network.TrackSearch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<TrackSearch> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrackSearch::class.java)
    }

    single {
        androidContext().getSharedPreferences(App.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory {
        Gson()
    }

    factory {
        MediaPlayer()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(
            searchTrackApi = get()
        )
    }

    factory {
        FavouriteMap()
    }

    factory {
        PlaylistMapper()
    }

    factory {
        PlaylistConverter()
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}