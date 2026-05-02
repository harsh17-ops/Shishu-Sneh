package com.shishusneh.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shishusneh.app.ui.components.LoadingView
import com.shishusneh.app.ui.navigation.AppNavGraph
import com.shishusneh.app.ui.screens.auth.AuthScreen
import com.shishusneh.app.ui.screens.settings.ProfileEditorScreen
import com.shishusneh.app.ui.theme.ShishuSnehTheme
import com.shishusneh.app.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state by mainViewModel.uiState.collectAsStateWithLifecycle()
            val darkTheme = when (state.settings.themeMode) {
                "light" -> false
                "dark" -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission(),
                onResult = {}
            )

            LaunchedEffect(state.settings.languageTag) {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(state.settings.languageTag)
                )
            }
            LaunchedEffect(state.settings.notificationsEnabled) {
                if (
                    state.settings.notificationsEnabled &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                ) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }

            ShishuSnehTheme(darkTheme = darkTheme) {
                when {
                    state.loading -> LoadingView()
                    state.currentUserId == null -> AuthScreen()
                    !state.hasProfile -> ProfileEditorScreen(onSaved = {})
                    else -> AppNavGraph()
                }
            }
        }
    }
}
