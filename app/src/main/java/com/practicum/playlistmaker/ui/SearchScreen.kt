package com.practicum.playlistmaker.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.ui.components.SearchBar
import com.practicum.playlistmaker.ui.components.Track
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun SearchScreen(modifier: Modifier) {
    Row {
        Column {
            SearchBar(
                modifier = Modifier.padding(bottom = 24.dp),
                onSearchHandler = {},
                onResetHandler = {}
            )
            Track(pic = "")
        }
    }
}

@Preview(apiLevel = 35, showBackground = false, showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenPreview() {
    PlaylistMakerTheme {
        SearchScreen(modifier = Modifier)
    }
}


@Preview(apiLevel = 35, showBackground = false, showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenNightPreview() {
    PlaylistMakerTheme {
        SearchScreen(modifier = Modifier)
    }
}