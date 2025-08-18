package com.practicum.playlistmaker.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.presentation.state.TrackListState
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import kotlin.random.Random
import com.practicum.playlistmaker.search.domain.models.Track

@Composable
fun Track(
    pic: String,
    modifier: Modifier = Modifier,
    trackName: String = "Here Comes The Sun (New Remastered). Here Comes The Sun (New Remastered).",
    artistName: String = "The Beatles.",
    duration: String = "4:01",
    onPress: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable { onPress() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val model: Any = pic.ifBlank {
            R.drawable.track_placeholder
        }
        AsyncImage(
            modifier = Modifier
                .size(45.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.track_placeholder),
            contentDescription = trackName,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                text = trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row {
                Text(
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1F, fill = false),
                    text = artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(horizontal = 6.dp),
                        painter = painterResource(R.drawable.track_time_dot),
                        contentDescription = null
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodySmall,
                        text = duration
                    )
                }
            }
        }

        Icon(
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(width = 24.dp, height = 24.dp),
            painter = painterResource(id = R.drawable.forward_24),
            contentDescription = null
        )

    }
}

fun getTracksList(): SnapshotStateList<Track> {
    val list = mutableStateListOf<Track>()

    repeat(3) {
        val id = Random.nextLong(700, 1_000)
        list.add(
            Track(
                trackId = id,
                trackName = "$id: Here Comes The Sun (New Remastered). Here Comes The Sun (New Remastered).",
                artistName = "$id: The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.",
                trackTime = "4:01",
                artworkUrl100 = "",
                collectionName = "",
                country = "",
                inFavourite = true,
                primaryGenreName = "",
                previewUrl = "",
                releaseDate = ""
            )
        )
    }

    return list
}

@Composable
fun TracksList(
    viewModel: SearchViewModel,
    modifier: Modifier = Modifier,
    onTrackClick: (Track) -> Unit = {}
) {
    val tracksData by viewModel.getTracksState().observeAsState()
    tracksData?.let { state ->
        when(state) {
            is TrackListState.Loading -> {
                CircularIndicator()
            }
            is TrackListState.HistoryContent -> {
                if (state.data.isNotEmpty()) {
                    ShowHistoryTracks(state.data, onPress = onTrackClick) {
                        viewModel.clearHistory()
                    }
                }
            }
            is TrackListState.SearchContent -> {
                ShowTracks(state.data, onPress = onTrackClick)
            }
            is TrackListState.Error -> {
                //TODO при переносе экрана поиска не забыть, что нужно во viewModel работать с ErrorType,
                //TODO который определен в пакете ui.data.model, а пока, дабы проверить работу, оставляем со старого пакета
                //TODO вроде поправил, надо проверить
                when(state.error) {
                    ErrorType.EMPTY_DATA -> {
                        Error(
                            errorType = state.error,
                            pic = R.drawable.nothing_found,
                            text = R.string.search_error_nothing_found
                        )
                    }
                    ErrorType.CONNECTION_ERROR -> {
                        Error(
                            errorType = state.error,
                            pic = R.drawable.connection_problems,
                            text = R.string.search_error_internet,
                            handler = {
                                viewModel.repeatRequest()
                            }
                        )
                    }
                }

            }
            is TrackListState.Idle -> {}
        }
    }
}

@Composable
private fun ShowHistoryTracks(
    tracks: List<Track>,
    onPress: (Track) -> Unit = {},
    onClearHistory: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(R.string.search_results),
            modifier = Modifier.padding(top = 18.dp, start = 16.dp, bottom = 18.dp, end = 16.dp)
        )
        ShowTracks(tracks, onPress = onPress)
        AppButton(
            text = R.string.clear_history,
            handler = onClearHistory,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
        )
    }
}

@Composable
fun ShowTracks(
    tracks: List<Track>,
    onPress: (Track) -> Unit = {},
) {
    LazyColumn(modifier = Modifier) {
        items(tracks) { track ->
            Track(
                pic = track.artworkUrl100,
                trackName = track.trackName,
                artistName = track.artistName,
                duration = track.trackTime,
            ) {
                onPress(track)
            }
        }
    }
}

@Composable
fun TracksListDemo(
    modifier: Modifier = Modifier,
    list: MutableList<Track> = remember { getTracksList() }
) {

    LazyColumn(
        modifier = Modifier
    ) {
        items(list) { track ->
            Track(
                pic = track.artworkUrl100,
                trackName = track.trackName,
                artistName = track.artistName,
                duration = track.trackTime,
            ) {
                val clickIndex = list.indexOf(track)
                val firstItem = list.removeAt(clickIndex)
                list.add(0, firstItem)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TracksListPreview() {
    PlaylistMakerTheme {
        TracksListDemo()
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TracksListHistoryPreview(list: MutableList<Track> = remember { getTracksList() }) {
    PlaylistMakerTheme {
        //TracksListDemo()
        ShowHistoryTracks(tracks = list)
    }
}

@Preview(showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun TrackNightPreview() {
    PlaylistMakerTheme {
        Track(
            modifier = Modifier,
            pic = ""
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TrackPreview() {
    PlaylistMakerTheme {
        Track(
            modifier = Modifier,
            pic = ""
        )
    }
}