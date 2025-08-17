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


/*<item name="android:fontFamily">@font/ys_display_medium</item>
<item name="android:textFontWeight">500</item>
<item name="android:textColor">?attr/colorOnSecondary</item>
<item name="android:gravity">start|center</item>
<item name="android:backgroundTint">@color/transparent</item>
<item name="android:textAllCaps">false</item>
<item name="android:layout_marginEnd">100dp</item>
<item name="android:paddingStart">@dimen/setting_padding_s</item>*/



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