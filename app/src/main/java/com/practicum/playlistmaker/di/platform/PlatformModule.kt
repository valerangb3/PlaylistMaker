package com.practicum.playlistmaker.di.platform

import com.practicum.playlistmaker.network.domain.provider.NetworkStatusProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import com.practicum.playlistmaker.platform.provider.AndroidNetworkStatusProvider

val platformModule = module {
    single <NetworkStatusProvider> {
        AndroidNetworkStatusProvider(androidContext())
    }
}