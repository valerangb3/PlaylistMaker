package com.practicum.playlistmaker.data

import android.media.MediaPlayer

class PlayerManager {

    val mediaPlayer = MediaPlayer()

    fun prepare(url: String, prepareHandle: () -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            prepareHandle()
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
    }

    fun pausePlayer() {
        mediaPlayer.pause()
    }

    fun setCompleteHandler(complete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            complete()
        }
    }

    fun getCurrentPosition(): Long = mediaPlayer.currentPosition.toLong()

    fun release() {
        mediaPlayer.release()
    }
}