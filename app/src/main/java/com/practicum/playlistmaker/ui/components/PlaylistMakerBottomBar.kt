package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.data.model.Screen
import com.practicum.playlistmaker.ui.theme.yp_black
import com.practicum.playlistmaker.ui.theme.yp_blue
import com.practicum.playlistmaker.ui.theme.yp_gray_light

@Composable
fun PlaylistMakerBottomBar(
    modifier: Modifier = Modifier,
    screen: Screen = Screen.MEDIA,
    onSearchItem: () -> Unit = {},
    onMediaItem: () -> Unit = {},
    onSettingsItem: () -> Unit = {},
) {
    val borderColor = yp_gray_light
    BottomAppBar(
        modifier = modifier
            .height(56.dp)
            .drawBehind {
                val strokeWidthPx = 1.dp.toPx()
                drawLine(
                    color = borderColor,
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = size.width, y = 0f),
                    strokeWidth = strokeWidthPx
                )
            },
        containerColor = MaterialTheme.colorScheme.surface,
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.height(56.dp).fillMaxWidth()

            ) {
                Box(
                    modifier = Modifier.clickable { onSearchItem() },
                    contentAlignment = Alignment.Center
                )
                {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.menu_item_search_new),
                            contentDescription = null,
                            tint = if (screen == Screen.SEARCH) yp_blue else LocalContentColor.current
                        )
                        Text(
                            text = stringResource(R.string.main_menu_search),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (screen == Screen.SEARCH) yp_blue else LocalContentColor.current
                        )
                    }
                }
                Box(
                    modifier = Modifier.clickable { onMediaItem() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.menu_item_media_new),
                            contentDescription = null,
                            tint = if (screen == Screen.MEDIA) yp_blue else LocalContentColor.current
                        )
                        Text(
                            text = stringResource(R.string.main_menu_media),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (screen == Screen.MEDIA) yp_blue else LocalContentColor.current
                        )
                    }
                }
                Box(
                    modifier = Modifier.clickable { onSettingsItem() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.menu_item_settings_new),
                            contentDescription = null,
                            tint = if (screen == Screen.SETTINGS) yp_blue else LocalContentColor.current
                        )
                        Text(
                            text = stringResource(R.string.main_menu_settings),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (screen == Screen.SETTINGS) yp_blue else LocalContentColor.current
                        )
                    }
                }
            }
        },
        windowInsets = WindowInsets(0.dp)
    )

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PlaylistMakerBottomBarPreview() {
    PlaylistMakerTheme {
        PlaylistMakerBottomBar()
    }
}