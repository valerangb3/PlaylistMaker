package com.practicum.playlistmaker.track.history

import android.content.Context
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.Track

const val HISTORY_KEY = "track_list_history"

//Класс предназначен только для работы со списком треков из истории
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

    fun addToBegin(track: Track, duplicatePosition: Int?) {
        val position = duplicatePosition ?: -1
        if (position > -1) {
            trackListHistory.removeAt(position)
        } else {
            removeDuplicate(track)
        }
        addToHistory(track)
    }

    fun addToHistory(track: Track) {
        trackListHistory.add(0, track)
        //избавляемся от возможных дубляжей
        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    fun removeDuplicate(track: Track) {
        val duplicatePosition = getPositionDuplicate(track)
        trackListHistory.removeAt(duplicatePosition)
        if (trackListHistory.size > COUNT_ITEMS) {
            trackListHistory.removeAt(trackListHistory.lastIndex)
        }
    }

    fun removeTrackHistory() {
        trackListHistory.clear()
        sharedPreferences.edit()
            .remove(HISTORY_KEY)
            .apply()
//        saveTrackHistory(trackListHistory)
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