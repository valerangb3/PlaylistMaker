package com.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = yp_gray,
    primaryContainer = yp_gray_light,
    onPrimary = yp_white,
    secondary = yp_blue,
    secondaryContainer = yp_blue_light,
    onSecondary = yp_black,
)

private val DarkColors = darkColorScheme(
    primary = yp_gray,
    primaryContainer = yp_gray_light,
    onPrimary = yp_black,
    secondary = yp_blue,
    secondaryContainer = yp_blue_light,
    onSecondary = yp_white,
)

@Composable
fun PlaylistMakerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content,
    )
}