package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.data.ThemeManager
import com.practicum.playlistmaker.domain.api.ThemeInteractor

class ThemeInteractorImpl(
    private val themeManager: ThemeManager
): ThemeInteractor {

    override fun getTheme(): Boolean = themeManager.getTheme()

    override fun saveTheme(saveTheme: Boolean) {
        themeManager.saveTheme(saveTheme)
    }

    override fun switchTheme(newTheme: Boolean) {
        themeManager.switchTheme(newTheme)
    }
}