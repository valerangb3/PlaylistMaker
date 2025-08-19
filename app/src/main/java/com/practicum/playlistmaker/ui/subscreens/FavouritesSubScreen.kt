package com.practicum.playlistmaker.ui.subscreens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.components.FavouriteList
import com.practicum.playlistmaker.ui.components.TracksListDemo
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavouritesSubScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinViewModel(),
    onTrackClick: (Track) -> Unit = {}
) {
    FavouriteList(
        modifier = modifier.fillMaxSize(),
        viewModel = viewModel,
        onTrackClick = onTrackClick
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FavouritesSubScreenDemoPreview(modifier: Modifier = Modifier) {
    PlaylistMakerTheme {
        TracksListDemo(modifier = modifier.fillMaxSize())
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FavouritesSubScreenPreview() {
    PlaylistMakerTheme {
        TracksListDemo()
    }
}