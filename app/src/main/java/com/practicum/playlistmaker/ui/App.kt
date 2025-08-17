package com.practicum.playlistmaker.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.ui.components.PlaylistMakerAppBar
import com.practicum.playlistmaker.ui.screens.SearchScreen
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.compose.koinViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.components.PlaylistMakerBottomBar
import com.practicum.playlistmaker.ui.data.model.Screen
import com.practicum.playlistmaker.ui.screens.MediaScreen

@Composable
fun App(
    viewModel: SearchViewModel = koinViewModel(),
    screen: Screen = Screen.MEDIA,
    onSearchItem: () -> Unit = {},
    onMediaItem: () -> Unit = {},
    onSettingsItem: () -> Unit = {},
) {
    val titleRes = when(screen) {
        Screen.MEDIA -> R.string.main_menu_media
        Screen.SEARCH -> R.string.main_menu_search
        Screen.SETTINGS -> R.string.main_menu_settings
    }
    PlaylistMakerTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { PlaylistMakerAppBar(title = stringResource(titleRes)) },
            bottomBar = {
                PlaylistMakerBottomBar(
                    onSearchItem = onSearchItem,
                    onMediaItem = onMediaItem,
                    onSettingsItem = onSettingsItem
                )
            }
        ) { paddingValues ->
            when(screen) {
                Screen.SEARCH -> {
                    SearchScreen(
                        viewModel = viewModel,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxHeight()
                    )
                }
                Screen.MEDIA -> {
                    MediaScreen()
                }
                Screen.SETTINGS -> {

                }
            }

        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AppPreview() {
    PlaylistMakerTheme {
        App()
    }
}