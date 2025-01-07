package com.practicum.playlistmaker.settings.domain

interface SettingsInteractor {
    fun getTheme(): Boolean
    fun switchTheme(newTheme: Boolean)
}