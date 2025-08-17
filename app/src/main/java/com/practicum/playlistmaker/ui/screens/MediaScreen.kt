package com.practicum.playlistmaker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun MediaScreen() {
    // CoroutineScope нам нужен для анимации при переключении табов
    val scope = rememberCoroutineScope()

    // Ключевое состояние Pager, при инициализации которого указывается количество экранов, которые можно переключать
    val pagerState = rememberPagerState(pageCount = { 2 })

    // Индекс текущего выбранного таба
    val selectedTabIndex = remember { pagerState.currentPage }

    Column(
        modifier = Modifier
            .fillMaxSize()
        //.padding(top = it.calculateTopPadding())
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.outline,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = "Первый экран") },
            )

            Tab(
                selected = selectedTabIndex == 1,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.outline,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = "Второй экран") },
            )
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when(page) {
                0 -> { Text("Fist") }
                1 -> { Text("Second") }
            }
        }
    }
}

@Preview
@Composable
fun MediaScreenPreview() {
    MediaScreen()
}