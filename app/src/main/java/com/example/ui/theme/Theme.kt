package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF1C1C1E),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF5E5CE6),
    background = Color.Black,
    onBackground = Color.White,
    surface = Color(0xFF1C1C1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF1C1C1E),
    onSurfaceVariant = Color.White,
    outline = Color(0xFF38383A),
    outlineVariant = Color(0xFF38383A),
    error = Color(0xFFFF453A),
    secondaryContainer = Color(0xFF2C2C2E),
    onSecondaryContainer = Color.White,
    tertiaryContainer = Color(0xFF2C2C2E),
    onTertiaryContainer = Color.White,
    errorContainer = Color(0xFF2C2C2E),
    onErrorContainer = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color.White,
    primaryContainer = Color.White,
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF5856D6),
    background = Color(0xFFF2F2F7),
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color.White,
    onSurfaceVariant = Color.Black,
    outline = Color(0xFFC6C6C8),
    outlineVariant = Color(0xFFC6C6C8),
    error = Color(0xFFFF3B30),
    secondaryContainer = Color.White,
    onSecondaryContainer = Color.Black,
    tertiaryContainer = Color.White,
    onTertiaryContainer = Color.Black,
    errorContainer = Color.White,
    onErrorContainer = Color.Black
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
