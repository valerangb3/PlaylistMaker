package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.ui.theme.yp_black
import com.practicum.playlistmaker.ui.theme.yp_blue

@Composable
fun CircularIndicator(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 134.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(44.dp),
            color = yp_blue,
            trackColor = Color.Transparent,
        )
    }
}

@Preview
@Composable
fun CircularIndicatorPreview() {
    CircularIndicator()
}