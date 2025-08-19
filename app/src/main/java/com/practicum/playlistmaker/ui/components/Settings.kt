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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.ui.theme.PlaylistMakerTheme
import com.practicum.playlistmaker.ui.theme.yp_blue
import com.practicum.playlistmaker.ui.theme.yp_blue_light
import org.koin.androidx.compose.koinViewModel

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    trackWidth: Dp = 32.dp,
    trackHeight: Dp = 12.dp,
    thumbSize: Dp = 18.dp,
    enabled: Boolean = true
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize else 0.dp,
        label = "thumbOffset"
    )

    val trackColor = if (checked) yp_blue_light else MaterialTheme.colorScheme.primary
    val thumbColor = if (checked) yp_blue else MaterialTheme.colorScheme.onPrimary

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
                .align(Alignment.CenterStart)
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
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.displayMedium,
            text = stringResource(itemText),
            modifier = Modifier.width(280.dp)
        )
        content()
    }
}

@Composable
fun Settings(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    onSwitchTheme: (isChecked: Boolean) -> Unit = {},
    onShareApp: () -> Unit = {},
    onTechnicalSupportClick: () -> Unit = {},
    onAgreeClick: () -> Unit = {},
) {
    val isDarkState by viewModel.themeState().observeAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SettingsItem(itemText = R.string.settings_menu_dark_theme) {
            CustomSwitch(
                checked = isDarkState ?: false,
                onCheckedChange = {
                    onSwitchTheme(it)
                }
            )
        }
        SettingsItem(itemText = R.string.settings_menu_share) {
            Icon(
                modifier = Modifier.clickable { onShareApp() },
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(R.drawable.share_24),
                contentDescription = null
            )
        }
        SettingsItem(itemText = R.string.settings_menu_support) {
            Icon(
                modifier = Modifier.clickable { onTechnicalSupportClick() },
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(R.drawable.support_24),
                contentDescription = null
            )
        }
        SettingsItem(itemText = R.string.settings_menu_user_agreement) {
            Icon(
                modifier = Modifier.clickable { onAgreeClick() },
                tint = MaterialTheme.colorScheme.onPrimary,
                painter = painterResource(R.drawable.forward_24),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun SettingsPreview() {
    PlaylistMakerTheme {
        Settings()
    }
}