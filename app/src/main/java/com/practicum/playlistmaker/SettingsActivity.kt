package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var switcher: SwitchMaterial
    private lateinit var buttonBack: Button
    private lateinit var buttonShare: TextView
    private lateinit var buttonSupport: TextView
    private lateinit var forwardButton: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appInstance = (applicationContext as App)

        setContentView(R.layout.activity_settings)
        buttonBack = findViewById(R.id.button_back)
        buttonBack.setOnClickListener {
            finish()
        }
        switcher = findViewById(R.id.themeSwitcher)
        switcher.isChecked = appInstance.isDarkTheme()
        appInstance.saveTheme()
        switcher.setOnCheckedChangeListener { _, checked ->
            appInstance.switchTheme(checked)
        }
        buttonShare = findViewById(R.id.share)
        buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_link))
            intent.setType("text/plain")
            startActivity(intent)
        }
        buttonSupport = findViewById(R.id.support)
        buttonSupport.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)

            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(R.string.settings_email_student))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_email_subject))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_email_text))
            startActivity(intent)
        }
        forwardButton = findViewById(R.id.forward)
        forwardButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.settings_offer_link))
            startActivity(intent)
        }
    }
}