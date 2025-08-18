package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.state.FavoriteState
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.compose.koinViewModel


@Composable
fun FavouriteList(
    viewModel: FavoriteViewModel,
    modifier: Modifier = Modifier,
    onTrackClick: (Track) -> Unit = {}
) {
    val favouriteData by viewModel.getScreenStateLiveData().observeAsState()

    favouriteData?.let { state ->
        when(state) {
            is FavoriteState.EmptyContent -> Error(
                errorType = ErrorType.EMPTY_DATA,
                pic = R.drawable.nothing_found,
                text = R.string.favorites_empty
            )
            is FavoriteState.FavouriteContent -> ShowTracks(tracks = viewModel.mapToTrackList(state.data), onPress = onTrackClick)
            is FavoriteState.Loading -> CircularIndicator()
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun FavouriteListPreview() {
    PlaylistMakerTheme {

    }
}

