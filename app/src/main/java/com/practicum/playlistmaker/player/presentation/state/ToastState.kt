package com.practicum.playlistmaker.player.presentation.state

sealed interface ToastState {
    object None: ToastState
    data class Show(val additionalMessage: String, val inPlaylist: Boolean): ToastState
}