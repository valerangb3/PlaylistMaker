package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.BrowserLinkData
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.sharing.domain.model.ShareLinkData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator
): SharingInteractor {

    companion object {

        private val EMAIL_LIST = arrayOf("karev.valera2013@yandex.ru")
        private const val EMAIL_SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val EMAIL_TEXT = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val EMAIL_URI = "mailto:"

        private const val BROWSER_URL = "https://yandex.ru/legal/practicum_offer/"

        private const val SHARE_URL = "https://practicum.yandex.ru/android-developer/?from=catalog"
        private const val SHARE_MIME_TYPE = "text/plain"
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsBrowserLinkData())
    }

    override fun shareApp() {
        externalNavigator.shareLink(getAppShareLinkData())
    }

    private fun getSupportEmailData(): EmailData = EmailData(
        emailList = EMAIL_LIST,
        emailSubject = EMAIL_SUBJECT,
        emailText = EMAIL_TEXT,
        uri = EMAIL_URI
    )

    private fun getTermsBrowserLinkData(): BrowserLinkData = BrowserLinkData(
        url = BROWSER_URL
    )

    private fun getAppShareLinkData(): ShareLinkData = ShareLinkData(
        url = SHARE_URL,
        mimeType = SHARE_MIME_TYPE
    )
}