package com.practicum.playlistmaker.ui.data.model

import androidx.annotation.StringRes
import com.practicum.playlistmaker.R

enum class MediaSubScreens(
    val stringResTabText: Int
) {
    FAVOURITES(stringResTabText = R.string.favorites_tab),
    PLAYLISTS(stringResTabText = R.string.playlist_tab)
}