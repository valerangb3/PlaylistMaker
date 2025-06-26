package com.practicum.playlistmaker.player.domain.models

import java.text.SimpleDateFormat
import java.util.Locale

data class PlayStatus(
    val progress: Long,
    val isPlaying: Boolean,
    val isPrepared: Boolean = false
) {
    fun formatProgress(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(progress)
    }
}
