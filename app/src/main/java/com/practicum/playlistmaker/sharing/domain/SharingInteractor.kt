package com.practicum.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp()
    fun sharePlaylist(content: String)
    fun openTerms()
    fun openSupport()
}