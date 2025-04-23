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
        private const val EMAIL_URI = "mailto:"

        private const val SHARE_MIME_TYPE = "text/plain"
    }

    private fun getSupportEmailData(): EmailData = EmailData(
        emailList = arrayOf(context.getString(R.string.settings_email_student)),
        emailSubject = context.getString(R.string.settings_email_subject),
        emailText = context.getString(R.string.settings_email_text),
        uri = EMAIL_URI
    )

    private fun getTermsBrowserLinkData(): BrowserLinkData = BrowserLinkData(
        url = context.getString(R.string.settings_offer_link)
    )

    private fun getAppShareLinkData(): ShareLinkData = ShareLinkData(
        url = context.getString(R.string.settings_share_link),
        mimeType = SHARE_MIME_TYPE
    )

    override fun openEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val emailData = getSupportEmailData()

            data = Uri.parse(emailData.uri)
            putExtra(Intent.EXTRA_EMAIL, emailData.emailList)
            putExtra(Intent.EXTRA_SUBJECT, emailData.emailSubject)
            putExtra(Intent.EXTRA_TEXT, emailData.emailText)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)

    }

    override fun openLink() {
        val browserLinkData = getTermsBrowserLinkData()

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(browserLinkData.url)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun shareLink() {
        val shareLinkData = getAppShareLinkData()

        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, shareLinkData.url)
            setType(shareLinkData.mimeType)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    override fun sharePlaylist(content: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, content)
            setType(SHARE_MIME_TYPE)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}