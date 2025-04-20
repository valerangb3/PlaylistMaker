package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {

    override fun openSupport() {
        externalNavigator.openEmail()
    }

    override fun openTerms() {
        externalNavigator.openLink()
    }

    override fun shareApp() {
        externalNavigator.shareLink()
    }

    override fun sharePlaylist(content: String) {
        externalNavigator.sharePlaylist(content = content)
    }
}