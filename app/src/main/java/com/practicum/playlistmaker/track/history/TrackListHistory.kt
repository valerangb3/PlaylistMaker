package com.practicum.playlistmaker.track.history

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.Track

const val HISTORY_KEY = "track_list_history"

class TrackListHistory(
    context: Context
) {
    private val trackListHistory = mutableListOf<Track>()
    private val sharedPreferences = (context as App).getSharedPreferences()

    init {
        restoreHistoryList()
    }

    //Функция для поиска индекса дубля в списке
    fun getPositionDuplicate(track: Track): Int {
        var index = -1
        if (trackListHistory.isNotEmpty()) {
            trackListHistory.forEachIndexed { i, duplicateTrack ->
                if (duplicateTrack.trackId == track.trackId) {
                    index = i
                }
            }
        }
        return index
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

    companion object {
        const val COUNT_ITEMS = 10
    }
}