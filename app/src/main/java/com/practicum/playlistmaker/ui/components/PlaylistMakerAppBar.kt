package com.practicum.playlistmaker.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistMakerAppBar(
    modifier: Modifier = Modifier,
    title: String
) {
    TopAppBar(
        modifier = modifier,
        title = { Title(text = title) },
        navigationIcon = {}
    )
}