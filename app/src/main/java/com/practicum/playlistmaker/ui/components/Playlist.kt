package com.practicum.playlistmaker.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.practicum.playlistmaker.media.presentation.state.PlaylistState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.models.Playlist
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.utils.getWordForm
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random
import kotlin.random.nextLong


fun getPlaylistItems(): List<Playlist> {
    val list = mutableStateListOf<Playlist>()

    repeat(3) {
        list.add(
            Playlist(
                id = Random.nextLong(from = 500, until = 1_000),
                title = "Best songs 2021",
                count = 35,
                filePath =  ""
            )
        )
    }
    return list
}

@Composable
fun ShowPlaylist(
    modifier: Modifier = Modifier,
    items: List<Playlist>,
    onPlaylistClick: (playlistId: Long) -> Unit = {},
    onCreatePlaylist:  () -> Unit = {},
) {
    Column {
        Spacer(modifier = Modifier.size(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppButton(
                text = R.string.playlist_add,
                handler = onCreatePlaylist
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                PlaylistItem(
                    id = item.id,
                    title = item.title,
                    description = getWordForm(count = item.count),
                    pic = item.filePath,
                    onPlaylistClick = onPlaylistClick
                )
            }
        }
    }

}

@Composable
fun Playlist(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel = koinViewModel(),
    onPlaylistClick: (playlistId: Long) -> Unit = {},
    onCreatePlaylist: () -> Unit = {},
) {
    val playlistState by viewModel.getScreenStateLiveData().observeAsState()
    playlistState?.let { state ->
        when (state) {
            is PlaylistState.EmptyContent -> {

                Column {
                    Spacer(modifier = Modifier.size(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppButton(
                            text = R.string.playlist_add,
                            handler = onCreatePlaylist
                        )
                    }
                    Error(
                        errorType = ErrorType.EMPTY_DATA,
                        pic = R.drawable.nothing_found,
                        text = R.string.playlist_empty
                    )
                }
            }
            is PlaylistState.Loading -> CircularIndicator()
            is PlaylistState.PlaylistContent -> {
                ShowPlaylist(
                    modifier = Modifier,
                    items = state.data,
                    onPlaylistClick = onPlaylistClick,
                    onCreatePlaylist = onCreatePlaylist
                )
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ShowPlaylistPreview(modifier: Modifier = Modifier, items: List<Playlist> = remember { getPlaylistItems() }) {
    PlaylistMakerTheme {
        ShowPlaylist(modifier = modifier, items = items)
    }
}

@Composable
fun PlaylistItem(
    id: Long,
    title: String,
    description: String,
    pic: String,
    onPlaylistClick: (playlistId: Long) -> Unit = {}
) {
    val model: Any = pic.ifBlank {
        R.drawable.track_placeholder
    }
    Column(
        modifier = Modifier
            .width(160.dp)
            .clickable { onPlaylistClick(id) }
    ) {
        AsyncImage(
            modifier = Modifier
                .height(160.dp)
                .clip(MaterialTheme.shapes.small),
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .build(),
            placeholder = painterResource(R.drawable.track_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            text = description,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaylistItemPreview() {
    PlaylistMakerTheme {
        PlaylistItem(
            id = 7L,
            title = "Best songs 2021",
            description = "98 треков",
            pic = ""
        )
    }
}


