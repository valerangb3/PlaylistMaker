package com.practicum.playlistmaker.ui.components

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.shapes


@Composable
fun AppButton(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
    handler: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = handler,
        shape = shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        )

    ) {
        Text(
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.surface,
            text = stringResource(text)
        )
    }
}

@Preview
@Composable
fun AppButtonPreview() {
    PlaylistMakerTheme { AppButton(text = R.string.update_request) { } }
}