package com.app.vibess.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary   = md_theme_light_primary,
    onPrimary = md_theme_light_onPrimary,
    // background = …, surface = … и т.д.
)

private val DarkColors = darkColorScheme(
    primary   = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    // background = …, surface = … и т.д.
)

@Composable
fun VibesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography  = androidx.compose.material3.Typography(),  // дефолтная типографика
        shapes      = androidx.compose.material3.Shapes(),      // дефолтные формы
        content     = content
    )
}
