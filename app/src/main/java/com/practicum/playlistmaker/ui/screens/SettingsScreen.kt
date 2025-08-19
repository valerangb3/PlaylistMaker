package com.practicum.playlistmaker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import com.practicum.playlistmaker.ui.components.Settings
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
    onSwitchTheme: (isChecked: Boolean) -> Unit,
    onShareApp: () -> Unit = {},
    onTechnicalSupportClick: () -> Unit = {},
    onAgreeClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        Settings(
            modifier = Modifier,
            viewModel = viewModel,
            onSwitchTheme = onSwitchTheme,
            onShareApp = onShareApp,
            onTechnicalSupportClick = onTechnicalSupportClick,
            onAgreeClick = onAgreeClick
        )
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    //SettingsScreen(onSwitchTheme = {})
}
