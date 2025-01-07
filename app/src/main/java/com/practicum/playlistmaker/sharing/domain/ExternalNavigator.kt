package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.sharing.domain.model.BrowserLinkData
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.sharing.domain.model.ShareLinkData

interface ExternalNavigator {
    fun shareLink(shareLinkData: ShareLinkData)
    fun openLink(browserLinkData: BrowserLinkData)
    fun openEmail(emailData: EmailData)
}