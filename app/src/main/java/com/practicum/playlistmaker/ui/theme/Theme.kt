package com.practicum.playlistmaker.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    surface = yp_white,
    onSurface = yp_black,

    primary = yp_gray_light,
    onPrimary = yp_gray,

    primaryContainer = yp_gray_light,
    onPrimaryContainer = black,


    secondary = yp_gray_light,
    onSecondary = yp_gray,
)

private val DarkColors = darkColorScheme(
    surface = yp_black,
    onSurface = yp_white,

    primary = yp_black,
    onPrimary = yp_white,

    primaryContainer = yp_black,
    onPrimaryContainer = yp_white,


    secondary = yp_white,
    onSecondary = yp_black,
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