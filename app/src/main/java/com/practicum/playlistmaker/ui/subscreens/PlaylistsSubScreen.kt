package com.practicum.playlistmaker.ui.subscreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.ui.components.Playlist
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistsSubScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = koinViewModel(),
    onPlaylistClick: (playlistId: Long) -> Unit = {},
    onCreatePlaylist: () -> Unit = {},
) {
    Playlist(
        modifier = modifier,
        viewModel = viewModel,
        onPlaylistClick = onPlaylistClick,
        onCreatePlaylist = onCreatePlaylist,
    )
}

@Preview
@Composable
fun PlaylistsSubScreenPreview() {
    PlaylistsSubScreen()
}