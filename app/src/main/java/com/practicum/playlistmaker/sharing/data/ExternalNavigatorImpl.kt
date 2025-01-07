package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.BrowserLinkData
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.sharing.domain.model.ShareLinkData

class ExternalNavigatorImpl(
    private val context: Context
): ExternalNavigator {

    companion object {
        private const val DEFAULT_MIME_TYPE_SHARE = "text/plain"
    }

    override fun openEmail(emailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
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

    override fun openLink(browserLinkData: BrowserLinkData) {
        val url = browserLinkData.url.ifEmpty {
            context.getString(R.string.settings_offer_link)
        }
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun shareLink(shareLinkData: ShareLinkData) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            val url = shareLinkData.url.ifEmpty {
                context.getString(R.string.settings_share_link)
            }
            val mimeType = shareLinkData.mimeType.ifEmpty {
                DEFAULT_MIME_TYPE_SHARE
            }
            putExtra(Intent.EXTRA_TEXT, url)
            setType(mimeType)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}