package com.practicum.playlistmaker.settings.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var isDarkTheme = MutableLiveData(getTheme())

    fun themeState(): LiveData<Boolean> = isDarkTheme

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun supportApp() {
        sharingInteractor.openSupport()
    }

    fun termsApp() {
        sharingInteractor.openTerms()
    }

    fun getTheme(): Boolean = settingsInteractor.getTheme()

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
        isDarkTheme.postValue(checked)
    }
}