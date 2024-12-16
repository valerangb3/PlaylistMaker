package com.practicum.playlistmaker.domain.api

interface ThemeInteractor {
    fun getTheme(): Boolean
    fun saveTheme(saveTheme: Boolean)
    fun switchTheme(newTheme: Boolean)
}