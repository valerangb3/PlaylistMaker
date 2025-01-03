package com.practicum.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.models.Track

class TracksHistoryManager(
    private val sharedPreferences: SharedPreferences
) {
    private val trackListHistory = mutableListOf<Track>()

    companion object {
        const val COUNT_ITEMS = 10
        const val HISTORY_KEY = "track_list_history"
    }

    init {
        restoreHistoryList()
    }

    private fun restoreHistoryList() {
        val json = sharedPreferences.getString(HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            val history = Gson().fromJson(json, Array<Track>::class.java)
            if (!history.isNullOrEmpty()) {
                trackListHistory.addAll(history)
            }
        }
    }

    fun addToHistory(track: Track) {
        val position = trackListHistory.indexOfFirst { it.trackId == track.trackId }
        if (position > -1) {
            trackListHistory.removeAt(position)
        }
        trackListHistory.add(0, track)

        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    fun removeTrackHistory() {
        trackListHistory.clear()
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
    }

    fun saveTrackHistory(trackList: MutableList<Track>) {
        if (trackList.isNotEmpty()) {
            val history = Gson().toJson(trackList)
            sharedPreferences.edit()
                .putString(HISTORY_KEY, history)
                .apply()
        }
    }

    fun getTrackListHistory(): MutableList<Track> = trackListHistory
}
