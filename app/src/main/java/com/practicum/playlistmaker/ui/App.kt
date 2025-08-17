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

@Composable
fun App(
    viewModel: SearchViewModel = koinViewModel()
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { PlaylistMakerAppBar(title = stringResource(R.string.main_menu_search)) },
        bottomBar = {
            PlaylistMakerBottomBar()
        }
    ) { paddingValues ->
        SearchScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(paddingValues).fillMaxHeight()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AppPreview() {
    PlaylistMakerTheme {
        App()
    }
}