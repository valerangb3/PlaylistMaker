package com.practicum.playlistmaker.player.domain

interface PlayerInteractor {
    fun prepareTrack(url: String, events: TrackHandler)

    suspend fun playback()
    fun pause()
    fun release()

    interface TrackHandler {
        suspend fun onStart(isPlaying: Boolean, progress: Long)

        fun onComplete()
        fun onLoad()
        fun onPause()
        fun onProgress(progress: Long)
    }
}