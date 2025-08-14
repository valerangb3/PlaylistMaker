package com.practicum.playlistmaker.root.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import com.practicum.playlistmaker.ui.SearchScreen
import com.practicum.playlistmaker.ui.components.Track

class ComposeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen(modifier = Modifier)
        }
    }

}