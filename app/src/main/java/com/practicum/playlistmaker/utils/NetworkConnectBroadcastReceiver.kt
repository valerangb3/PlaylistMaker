package com.practicum.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.network.domain.CheckNetworkUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NetworkConnectBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    val checkNetworkUseCase: CheckNetworkUseCase by inject()

    override fun onReceive(p0: Context?, p1: Intent?) {
        p0?.let {
            val networkDisable = !checkNetworkUseCase.execute()
            if (networkDisable) {
                Toast.makeText(it, R.string.check_network, Toast.LENGTH_SHORT).show()
            }
        }
    }
}