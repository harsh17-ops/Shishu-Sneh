package com.shishusneh.app.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

data class AppSettings(
    val notificationsEnabled: Boolean = true,
    val languageTag: String = "en",
    val themeMode: String = "system"
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationsKey = booleanPreferencesKey("notifications_enabled")
    private val languageKey = stringPreferencesKey("language")
    private val themeModeKey = stringPreferencesKey("theme_mode")

    val settings: Flow<AppSettings> = context.settingsDataStore.data.map {
        AppSettings(
            notificationsEnabled = it[notificationsKey] ?: true,
            languageTag = it[languageKey] ?: "en",
            themeMode = it[themeModeKey] ?: "system"
        )
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[notificationsKey] = enabled }
    }

    suspend fun setLanguage(languageTag: String) {
        context.settingsDataStore.edit { it[languageKey] = languageTag }
    }

    suspend fun setThemeMode(mode: String) {
        context.settingsDataStore.edit { it[themeModeKey] = mode }
    }
}
