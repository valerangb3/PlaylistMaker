package com.practicum.playlistmaker.utils

import android.view.View
import androidx.core.view.isVisible

fun View.show() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}