package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme


@Composable
fun Playlist() {
    /*LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp)
    ) {
        items(itemsList) { item ->
            Text(
                text = "Item $item",
                fontSize = 20.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
    }*/
}

@Composable
fun PlaylistItem(
    title: String,
    description: String
) {
    //R.drawable.track_placeholder
    /*val model: Any = pic.ifBlank {
        R.drawable.track_placeholder
    }*/
    val model = R.drawable.ds
    Column {
        AsyncImage(
            modifier = Modifier
                .size(160.dp)
                .clip(MaterialTheme.shapes.small),
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            placeholder = painterResource(model),
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

@Preview
@Composable
fun PlaylistPreview() {
    Playlist()
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaylistItemPreview() {
    PlaylistMakerTheme {
        PlaylistItem(
            title = "Best songs 2021",
            description = "98 треков"
        )
    }
}


