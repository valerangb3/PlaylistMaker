package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
): SettingsInteractor {

    override fun getTheme(): Boolean = settingsRepository.getTheme()

    override fun switchTheme(newTheme: Boolean) {
        settingsRepository.switchTheme(newTheme)
    }
}