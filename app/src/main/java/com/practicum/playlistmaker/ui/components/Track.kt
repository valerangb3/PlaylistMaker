package com.practicum.playlistmaker.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Track(
    modifier: Modifier = Modifier,
    pic: String
) {
    /*Row(mod) {
        
    }*/
}

@Preview
@Composable
fun TrackPreview() {
    Track(
        modifier = Modifier,
        pic = ""
    )
}