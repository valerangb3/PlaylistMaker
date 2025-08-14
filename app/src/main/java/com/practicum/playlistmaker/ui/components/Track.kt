package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
                .clip(RoundedCornerShape(2.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(model)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.beatles),
            contentDescription = trackName,
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.weight(1F)
        ) {
            Text(
                text = trackName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row {
                Text(
                    modifier = Modifier.weight(1F),
                    text = artistName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = duration)
            }
        }

        Icon(
            modifier = Modifier.size(width = 24.dp, height = 24.dp),
            painter = painterResource(id = R.drawable.forward_24),
            contentDescription = null
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TrackPreview() {
    Track(
        modifier = Modifier,
        pic = ""
    )
}