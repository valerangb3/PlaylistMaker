package com.practicum.playlistmaker.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.yp_black
import com.practicum.playlistmaker.ui.theme.yp_blue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldCustom(
    value: String,
    contentPadding: PaddingValues,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),

) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    // If color is not provided via the text style, use content color as a default
    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            //colors.textColor(enabled, isError, focused)
            when {
                !enabled -> colors.disabledTextColor
                isError -> colors.errorTextColor
                focused -> colors.focusedTextColor
                else -> colors.unfocusedTextColor
            }
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    CompositionLocalProvider(LocalTextSelectionColors provides colors.textSelectionColors) {
        BasicTextField(
            value = value,
            modifier =
                modifier
                    .defaultMinSize(
                        minWidth = TextFieldDefaults.MinWidth,
                    ),
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(if (isError) colors.errorCursorColor else colors.cursorColor),
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            decorationBox =
                @Composable { innerTextField ->
                    // places leading icon, text field with label and placeholder, trailing icon
                    TextFieldDefaults.DecorationBox(
                        contentPadding = contentPadding,
                        value = value,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        placeholder = placeholder,
                        label = label,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        prefix = prefix,
                        suffix = suffix,
                        supportingText = supportingText,
                        shape = shape,
                        singleLine = singleLine,
                        enabled = enabled,
                        isError = isError,
                        interactionSource = interactionSource,
                        colors = colors
                    )
                }
        )
    }
}

//Отмечаю, что у TextField leading и trailing icon нельзя изменить ширину в рамках передачи параметров
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearchHandler: (String) -> Unit,
    onResetHandler: () -> Unit = {},
) {
    var searchText by remember { mutableStateOf("") }
    //val focusManager = LocalFocusManager.current
    //val keyboardController = LocalSoftwareKeyboardController.current


    TextFieldCustom(
        /*textStyle = TextStyle(
            fontWeight = FontWeight(400),
            fontSize = 16.sp
        ),*/
        contentPadding = PaddingValues(8.dp),
        textStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.ys_display_regular,
                    weight = FontWeight.W400
                )
            )
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
            //.height(36.dp)
            .onFocusChanged { focusState ->
                if (focusState.hasFocus && searchText.isEmpty()) {
                    onResetHandler()
                }
            },
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
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