package com.practicum.playlistmaker.search.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.repository.TracksHistoryRepository
import androidx.core.content.edit
import com.practicum.playlistmaker.favourites.domain.FavouriteRepository

class TracksHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson,
    private val favouriteRepository: FavouriteRepository
): TracksHistoryRepository {
    private val trackListHistory = mutableListOf<Track>()
    private var isRestore = false

    companion object {
        const val COUNT_ITEMS = 10
        const val HISTORY_KEY = "track_list_history"
    }

    private fun restoreHistoryList() {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            val history = gson.fromJson(json, Array<Track>::class.java)
            if (!history.isNullOrEmpty()) {
                trackListHistory.addAll(history)
            }
        }
    }

    override fun addToHistory(track: Track) {
        val position = trackListHistory.indexOfFirst { it.trackId == track.trackId }
        if (position > -1) {
            trackListHistory.removeAt(position)
        }
        trackListHistory.add(0, track)

        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    override suspend fun removeTrackHistory() {
        trackListHistory.clear()
        sharedPreferences.edit() {
            remove(HISTORY_KEY)
        }
    }

    override suspend fun saveTrackHistory(trackList: MutableList<Track>) {
        if (trackList.isNotEmpty()) {
            val history = gson.toJson(trackList)
            sharedPreferences.edit() {
                putString(HISTORY_KEY, history)
            }
        }
    }

    override suspend fun getTrackListHistory(): MutableList<Track> {
        if (!isRestore) {
            restoreHistoryList()
            isRestore = true
        }
        favouriteRepository.getFavouriteIdList().collect { favouriteList ->
            trackListHistory.map { trackHistory ->
                trackHistory.inFavourite = favouriteList.contains(trackHistory.trackId)
            }
        }
        return trackListHistory
    }
}
