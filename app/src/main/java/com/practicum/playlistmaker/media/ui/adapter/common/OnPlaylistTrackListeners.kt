package com.practicum.playlistmaker.media.ui.adapter.common

import com.practicum.playlistmaker.media.ui.models.Track

interface OnPlaylistTrackListeners {
    fun onClickHandler(track: Track)
    fun onLongClickHandler(track: Track)
}