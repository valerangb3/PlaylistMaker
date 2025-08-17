package com.practicum.playlistmaker.ui.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.presentation.models.ErrorType
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun Error(
    modifier: Modifier = Modifier,
    @DrawableRes pic: Int,
    @StringRes text: Int,
    errorType: ErrorType,
    handler: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 104.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(errorType) {
            ErrorType.EMPTY_DATA -> EmptyDataError(pic = pic, text = text)
            ErrorType.CONNECTION_ERROR -> ConnectionError(pic = pic, text = text, buttonErrorHandler = handler)
        }
    }
}



@Composable
private fun ConnectionError(
    modifier: Modifier = Modifier,
    @DrawableRes pic: Int,
    @StringRes text: Int,
    buttonErrorHandler: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = stringResource(text)
        Image(
            modifier = Modifier.size(120.dp),
            painter = painterResource(pic),
            contentDescription = text
        )
        Text(
            modifier = modifier.padding(
                start = 24.dp,
                end = 24.dp,
                top = 16.dp,
                bottom = 24.dp
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            text = text
        )
        AppButton(text = R.string.update_request, handler = buttonErrorHandler)

    }
}

@Composable
private fun EmptyDataError(
    modifier: Modifier = Modifier,
    @DrawableRes pic: Int,
    @StringRes text: Int
) {
    val text = stringResource(text)
    Image(
        modifier = Modifier.size(120.dp),
        painter = painterResource(pic),
        contentDescription = text
    )
    Text(
        modifier = modifier.padding(
            start = 24.dp,
            end = 24.dp,
            top = 16.dp,
            bottom = 24.dp
        ),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.bodyMedium,
        text = text
    )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorPreview() {
    PlaylistMakerTheme {
        Error(
            pic = R.drawable.connection_problems,
            text = R.string.search_error_internet,
            errorType = ErrorType.CONNECTION_ERROR
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ErrorNightPreview() {
    PlaylistMakerTheme {
        Error(
            pic = R.drawable.connection_problems,
            text = R.string.search_error_internet,
            errorType = ErrorType.CONNECTION_ERROR
        )
    }
}