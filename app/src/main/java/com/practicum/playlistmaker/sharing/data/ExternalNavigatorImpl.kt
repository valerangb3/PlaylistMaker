package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.model.BrowserLinkData
import com.practicum.playlistmaker.sharing.data.model.EmailData
import com.practicum.playlistmaker.sharing.data.model.ShareLinkData

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {

    companion object {
        private val EMAIL_LIST = arrayOf("karev.valera2013@yandex.ru")
        private const val EMAIL_SUBJECT = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
        private const val EMAIL_TEXT = "Спасибо разработчикам и разработчицам за крутое приложение!"
        private const val EMAIL_URI = "mailto:"

        private const val BROWSER_URL = "https://yandex.ru/legal/practicum_offer/"

        private const val SHARE_URL = "https://practicum.yandex.ru/android-developer/?from=catalog"
        private const val SHARE_MIME_TYPE = "text/plain"
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

    override fun openEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val emailData = getSupportEmailData()
            data = Uri.parse(emailData.uri)
            val emailList = if (emailData.emailList.isEmpty()) {
                arrayOf(context.getString(R.string.settings_email_student))
            } else {
                emailData.emailList
            }
            val emailSubject = emailData.emailSubject.ifEmpty {
                context.getString(R.string.settings_email_subject)
            }
            val emailText = emailData.emailText.ifEmpty {
                context.getString(R.string.settings_email_text)
            }
            putExtra(Intent.EXTRA_EMAIL, emailList)
            putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailText)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)

    }

    override fun openLink() {
        val browserLinkData = getTermsBrowserLinkData()

        val url = browserLinkData.url.ifEmpty {
            context.getString(R.string.settings_offer_link)
        }
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun shareLink() {
        val shareLinkData = getAppShareLinkData()

        val intent = Intent(Intent.ACTION_SEND).apply {
            val url = shareLinkData.url.ifEmpty {
                context.getString(R.string.settings_share_link)
            }
            val mimeType = shareLinkData.mimeType.ifEmpty {
                SHARE_MIME_TYPE
            }
            putExtra(Intent.EXTRA_TEXT, url)
            setType(mimeType)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}