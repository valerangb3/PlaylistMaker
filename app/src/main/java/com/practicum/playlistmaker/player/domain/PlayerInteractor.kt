package com.practicum.playlistmaker.player.domain

interface PlayerInteractor {
    fun prepareTrack(url: String, events: TrackHandler)

    fun playback()
    fun pause()
    fun release()

    interface TrackHandler {
        fun onComplete()
        fun onLoad()
        fun onStart()
        fun onPause()
        fun onProgress(progress: Long)
    }
}