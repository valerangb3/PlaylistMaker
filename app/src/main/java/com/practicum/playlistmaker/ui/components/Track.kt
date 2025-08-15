package com.practicum.playlistmaker.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.data.Track
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun Track(
    pic: String,
    trackName: String = "Here Comes The Sun (New Remastered). Here Comes The Sun (New Remastered).",
    artistName: String = "The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.",
    duration: String = "4:01",
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val model: Any = pic.ifBlank {
            R.drawable.beatles
        }
        AsyncImage(
            modifier = Modifier
                .size(45.dp)
                .clip(MaterialTheme.shapes.extraSmall),
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.beatles),
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
                        .weight(1F),
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

fun getTracksList(): List<Track> {
    val list = mutableListOf<Track>()
    //pic: String,
    //trackName: String = "Here Comes The Sun (New Remastered). Here Comes The Sun (New Remastered).",
    //artistName: String = "The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.",
    //duration: String = "4:01",

    repeat(3) {
        list.add(
            Track(
                trackId = 7L,
                trackName = "Here Comes The Sun (New Remastered). Here Comes The Sun (New Remastered).",
                artistName = "The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.The Beatles.",
                trackTime = "4:01",
                artworkUrl100 = ""
            )
        )
    }

    return list
}

@Composable
fun TracksList(
    modifier: Modifier = Modifier,
    list: List<Track> = remember { getTracksList() }
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
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TracksListPreview() {
    PlaylistMakerTheme {
        TracksList()
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