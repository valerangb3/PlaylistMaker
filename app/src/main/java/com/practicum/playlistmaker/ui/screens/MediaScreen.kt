package com.practicum.playlistmaker.ui.screens

import android.content.res.Configuration
import com.practicum.playlistmaker.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.media.presentation.viewmodel.FavoriteViewModel
import com.practicum.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.practicum.playlistmaker.ui.components.PlaylistMakerAppBar
import com.practicum.playlistmaker.ui.components.PlaylistMakerBottomBar
import com.practicum.playlistmaker.ui.data.model.MediaSubScreens
import com.practicum.playlistmaker.ui.subscreens.FavouritesSubScreen
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

//viewModel: FavoriteViewModel = koinViewModel()

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    content: @Composable (mediaSubScreen: MediaSubScreens) -> Unit
) {
    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(pageCount = { 2 })

    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }

    Column(
        modifier = modifier
            .fillMaxSize()
        //.padding(top = it.calculateTopPadding())
    ) {
        TabRow(
            containerColor = MaterialTheme.colorScheme.surface,
            selectedTabIndex = selectedTabIndex.value,
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(48.dp),
            divider = {},
            indicator = { tabPositions ->
                Row(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.value]),
                    horizontalArrangement = Arrangement.Center // Center the indicator if smaller
                ) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .widthIn(148.dp)
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                }
            }
        ) {
            MediaSubScreens.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedTabIndex.value == index,
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = MaterialTheme.colorScheme.outline,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(destination.stringResTabText),
                        color = MaterialTheme.colorScheme.onSurface
                    ) },
                )
            }
        }
        HorizontalPager(
            verticalAlignment = Alignment.Top,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) { page ->
            when(page) {
                0 -> {
                    content(MediaSubScreens.FAVOURITES)
                }
                1 -> {
                    content(MediaSubScreens.PLAYLISTS)
                }
            }
        }
    }
}


@Preview
@Composable
fun MediaScreenPreview() {
    PlaylistMakerTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { PlaylistMakerAppBar(title = stringResource(R.string.main_menu_media)) },
            bottomBar = {
                PlaylistMakerBottomBar(
                    onSearchItem = {},
                    onMediaItem = {},
                    onSettingsItem = {}
                )
            }
        ) { paddingValues ->
            MediaScreen(modifier = Modifier.padding(paddingValues)) {}
        }
    }
    //MediaScreen(modifier = Modifier, viewModel = koinViewModel())
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun MediaScreenNightPreview() {
    PlaylistMakerTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = { PlaylistMakerAppBar(title = stringResource(R.string.main_menu_media)) },
            bottomBar = {
                PlaylistMakerBottomBar(
                    onSearchItem = {},
                    onMediaItem = {},
                    onSettingsItem = {}
                )
            }
        ) { paddingValues ->
            MediaScreen(modifier = Modifier
                .padding(paddingValues)
                .fillMaxHeight()
            ) {}
        }
    }
    //MediaScreen(modifier = Modifier, viewModel = koinViewModel())
}