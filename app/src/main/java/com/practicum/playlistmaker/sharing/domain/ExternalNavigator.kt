package com.practicum.playlistmaker.sharing.domain

interface ExternalNavigator {
    fun shareLink()
    fun sharePlaylist(content: String)
    fun openLink()
    fun openEmail()
}