package com.practicum.playlistmaker.media.domain.models

data class Playlist(
    //TODO добавить ид плейлиста, он нужен будет для того, чтобы передать его в карточку playlist'a
    val title: String,
    val filePath: String,
    val count: Int = 0,
)