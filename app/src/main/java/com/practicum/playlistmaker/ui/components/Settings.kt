package com.practicum.playlistmaker.ui.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R





@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    @StringRes itemText: Int,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .height(61.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(itemText),
            modifier = Modifier.width(280.dp)
        )
        content()
    }
}

@Composable
fun Settings(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsItem(itemText = R.string.settings_menu_dark_theme) {
            var checked by remember { mutableStateOf(true) }

            //Switch
            val switch = Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )
        }
        SettingsItem(itemText = R.string.settings_menu_share) {
            Icon(painter = painterResource(R.drawable.share_24), contentDescription = null)
        }
        SettingsItem(itemText = R.string.settings_menu_support) {
            Icon(painter = painterResource(R.drawable.support_24), contentDescription = null)
        }
        SettingsItem(itemText = R.string.settings_menu_user_agreement) {
            Icon(painter = painterResource(R.drawable.forward_24), contentDescription = null)
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    Settings()
}