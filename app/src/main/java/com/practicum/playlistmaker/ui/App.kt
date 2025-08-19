package com.practicum.playlistmaker.ui

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.practicum.playlistmaker.ui.components.PlaylistMakerAppBar
import com.practicum.playlistmaker.ui.screens.SearchScreen
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import org.koin.androidx.compose.koinViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.media.presentation.viewmodel.PlaylistViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.ui.components.PlaylistMakerBottomBar
import com.practicum.playlistmaker.ui.data.model.MediaSubScreens
import com.practicum.playlistmaker.ui.data.model.Screen
import com.practicum.playlistmaker.ui.screens.MediaScreen
import com.practicum.playlistmaker.ui.subscreens.FavouritesSubScreen
import com.practicum.playlistmaker.ui.subscreens.PlaylistsSubScreen

//Подход такой себе (в App определять такое большое кол-во лямбд),
// но в дальнейшем планирую изменить подход с переходом всего приложения на Compose
@Composable
fun App(
    screen: Screen = Screen.MEDIA,
    onSearchItem: () -> Unit = {},
    onMediaItem: () -> Unit = {},
    onSettingsItem: () -> Unit = {},
    onTrackClick: (track: Track) -> Unit = {},
    onPlaylistClick: (playlistId: Long) -> Unit = {},
    handler: () -> Unit = {}
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
                    screen = screen,
                    onSearchItem = onSearchItem,
                    onMediaItem = onMediaItem,
                    onSettingsItem = onSettingsItem
                )
            }
        ) { paddingValues ->

            when(screen) {
                Screen.SEARCH -> {
                    SearchScreen(
                        viewModel = koinViewModel(),
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxHeight(),
                        onTrackClick = onTrackClick
                    )
                }
                Screen.MEDIA -> {
                    MediaScreen(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxHeight(),
                    ) { mediaSubScreen ->
                        when(mediaSubScreen) {
                            MediaSubScreens.FAVOURITES -> {
                                Spacer(modifier = Modifier.height(16.dp))
                                val viewModel = koinViewModel<FavoriteViewModel>()
                                //пока такое решение, потом переделаю все на Flow
                                LaunchedEffect(Unit) {
                                    viewModel.getFavouriteList()
                                }
                                FavouritesSubScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier,
                                    onTrackClick = onTrackClick
                                )
                            }
                            MediaSubScreens.PLAYLISTS -> {
                                Spacer(modifier = Modifier.height(16.dp))
                                val viewModel = koinViewModel<PlaylistViewModel>()
                                //пока такое решение, потом переделаю все на Flow
                                LaunchedEffect(Unit) {
                                    viewModel.getPlaylistAll()
                                }
                                PlaylistsSubScreen(
                                    modifier = Modifier,
                                    viewModel = koinViewModel<PlaylistViewModel>(),
                                    onPlaylistClick = onPlaylistClick,
                                    onCreatePlaylist = handler
                                )
                            }
                        }
                    }
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
        App(
            screen = Screen.MEDIA
        )
    }
}