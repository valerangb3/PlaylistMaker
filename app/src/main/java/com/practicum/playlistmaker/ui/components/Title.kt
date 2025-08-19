package com.practicum.playlistmaker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Title(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(
        modifier = modifier.height(56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = text
        )
    }
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TitlePreview() {
    MaterialTheme {
        Title(text = "Поиск")
    }
}