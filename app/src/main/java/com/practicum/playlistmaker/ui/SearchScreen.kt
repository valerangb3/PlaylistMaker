package com.practicum.playlistmaker.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchScreen(modifier: Modifier) {
    Row {

    }
}

@Preview(apiLevel = 35, showBackground = false, showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SearchScreen(modifier = Modifier)
}