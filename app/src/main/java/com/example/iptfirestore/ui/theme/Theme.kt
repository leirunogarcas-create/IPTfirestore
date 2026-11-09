package com.example.iptfirestore.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF38080A),
    onPrimaryContainer = Color(0xFFFFD2D4),
    secondary = RedSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF450D10),
    onSecondaryContainer = Color(0xFFFFD2D4),
    tertiary = RedTertiary,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF331012),
    onTertiaryContainer = Color(0xFFFFDAD8),
    background = DarkBackground,
    onBackground = Color(0xFFF0F0F0),
    surface = DarkSurface,
    onSurface = Color(0xFFF0F0F0),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFE0E0E0),
    error = Color(0xFFFF5252),
    onError = Color.White,
    errorContainer = Color(0xFF5A1414),
    onErrorContainer = Color(0xFFFFDAD8)
)

private val LightColorScheme = lightColorScheme(
    primary = RedPrimaryVariant,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF38080A),
    onPrimaryContainer = Color(0xFFFFD2D4),
    secondary = RedPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF450D10),
    onSecondaryContainer = Color(0xFFFFD2D4),
    tertiary = RedSecondary,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = Color.White,
    surface = DarkSurface,
    onSurface = Color.White,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFE0E0E0),
    error = Color(0xFFFF5252),
    onError = Color.White
)

@Composable
fun IPTfirestoreTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
