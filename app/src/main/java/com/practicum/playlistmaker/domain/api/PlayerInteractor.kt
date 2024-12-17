package com.practicum.playlistmaker.domain.api

interface PlayerInteractor {
    fun startTrack()
    fun pauseTrack()
    fun prepare(url: String)
    fun release()
    fun playbackControl(start: () -> Unit, pause: () -> Unit, complete: () -> Unit)
    fun getCurrentPosition(): Long
}