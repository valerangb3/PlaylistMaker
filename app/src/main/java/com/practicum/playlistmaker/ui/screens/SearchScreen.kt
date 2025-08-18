package com.practicum.playlistmaker.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.ui.components.SearchBar
import com.practicum.playlistmaker.ui.components.TracksList
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    modifier: Modifier,
    onTrackClick: (track: Track) -> Unit = {}
) {
    Row(modifier = modifier) {
        Column {
            SearchBar(
                modifier = Modifier.padding(bottom = 24.dp),
                onSearchHandler = { newText ->
                    if (newText.isNotEmpty()) {
                        viewModel.searchDebounce(changedText = newText)
                    } else {
                        viewModel.showHistoryList()
                    }
                },
                onResetHandler = {
                    viewModel.showHistoryList()
                }
            )
            TracksList(
                viewModel = viewModel,
                onTrackClick = {
                    viewModel.addToHistory(it)
                    viewModel.saveHistory()
                    if (viewModel.clickDebounce()) {
                        onTrackClick(it)
                    }
                }
            )
        }
    }
}

@Preview(apiLevel = 35, showBackground = false, showSystemUi = true,
    uiMode = Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenPreview() {
    PlaylistMakerTheme {
        SearchScreen(modifier = Modifier, viewModel = koinViewModel())
    }
}


@Preview(apiLevel = 35, showBackground = false, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenNightPreview() {
    PlaylistMakerTheme {
        SearchScreen(modifier = Modifier, viewModel = koinViewModel())
    }
}