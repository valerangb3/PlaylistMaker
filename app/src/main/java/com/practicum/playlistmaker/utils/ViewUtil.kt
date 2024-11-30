package com.practicum.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.core.view.isVisible

fun View.show() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}