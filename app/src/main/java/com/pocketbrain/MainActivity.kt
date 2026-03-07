package com.pocketbrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.pocketbrain.data.preferences.PreferencesManager
import com.pocketbrain.ui.navigation.PocketBrainNavGraph
import com.pocketbrain.ui.theme.PocketBrainTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkTheme by preferencesManager.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())
            PocketBrainTheme(darkTheme = darkTheme) {
                PocketBrainNavGraph()
            }
        }
    }
}
