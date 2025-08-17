package com.practicum.playlistmaker.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.yp_black
import com.practicum.playlistmaker.ui.theme.yp_blue


//Отмечают, что у OutlinedTextField имеются внутренние отступы, на которые пока нет возможности повлиять напрямую
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchHandler: (String) -> Unit,
    onResetHandler: () -> Unit = {},
) {
    var searchText by remember { mutableStateOf("") }
    //val focusManager = LocalFocusManager.current
    //val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        textStyle = TextStyle(
            fontWeight = FontWeight(400),
            fontSize = 16.sp
        ),
        leadingIcon = {
            Icon(
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.size(16.dp),
                imageVector = ImageVector.vectorResource(R.drawable.search_icon),
                contentDescription = null
            )
        },
        trailingIcon = {
            if (!searchText.isEmpty()) {
                Icon(
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.clickable {
                        //focusManager.clearFocus()
                        //keyboardController?.hide()
                        searchText = ""
                        onResetHandler()
                    },
                    imageVector = ImageVector.vectorResource(R.drawable.clear_icon),
                    contentDescription = null
                )
            }
        },
        placeholder = {
            Text(
                color = MaterialTheme.colorScheme.onSecondary,
                text = "Поиск",
                style = TextStyle(
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.ys_display_regular))
                ),
            )
        },
        value = searchText,
        onValueChange = {
            searchText = it
            onSearchHandler(searchText)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (focusState.hasFocus && searchText.isEmpty()) {
                    onResetHandler()
                }
            },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            errorContainerColor = MaterialTheme.colorScheme.secondary,

            focusedTextColor = yp_black,
            unfocusedTextColor = yp_black,
            disabledTextColor = yp_black,
            errorTextColor = yp_black,

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,

            cursorColor = yp_blue
        )
    )
}

@Preview(
    showSystemUi = false, showBackground = true
)
@Composable
fun SearchPreview() {
    PlaylistMakerTheme {
        SearchBar(onSearchHandler = {})
    }
}


@Preview(
    showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchNightPreview() {
    PlaylistMakerTheme {
        SearchBar(onSearchHandler = {})
    }
}