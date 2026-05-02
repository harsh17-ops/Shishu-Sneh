package com.shishusneh.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    secondary = OrangeAccent,
    tertiary = TealSecondary,
    background = WarmBackground,
    surface = CardCream
)

private val DarkColors = darkColorScheme(
    primary = TealSecondary,
    secondary = OrangeAccent,
    tertiary = TealPrimary,
    background = DarkBackground,
    surface = DarkSurface
)

@Composable
fun ShishuSnehTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = AppTypography,
        content = content
    )
}
