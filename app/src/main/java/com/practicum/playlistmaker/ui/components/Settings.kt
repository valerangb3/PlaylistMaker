package com.practicum.playlistmaker.ui.components

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 52.dp,
    trackHeight: Dp = 20.dp,   // трек узкий
    thumbSize: Dp = 36.dp,     // а thumb больше трека 👈
    enabled: Boolean = true
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize else 0.dp,
        label = "thumbOffset"
    )

    // Цвета можно брать и из темы
    val trackColor = if (checked) Color(0xFFB0BEC5) else Color(0xFFECEFF1)
    val thumbColor = if (checked) Color.DarkGray else Color.LightGray

    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
        Box(
            modifier = modifier
                .width(trackWidth)
                .height(trackHeight)
                .clip(RoundedCornerShape(trackHeight / 2))
                .background(trackColor)
                .clickable(enabled = enabled) { onCheckedChange(!checked) }
        )

        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(thumbColor)
                .align(Alignment.CenterStart) // 👈 центрируем по вертикали, поэтому он выше трека
                .shadow(2.dp, CircleShape)
        )
    }

}




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

            CustomSwitch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                }
            )

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