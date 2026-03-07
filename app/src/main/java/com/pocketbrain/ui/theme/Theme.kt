package com.pocketbrain.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary          = Purple80,
    onPrimary        = Color(0xFF21005D),
    primaryContainer = PurpleGradientStart,
    secondary        = PurpleGrey80,
    background       = DarkBackground,
    surface          = DarkSurface,
    surfaceVariant   = DarkCard,
    onBackground     = Color(0xFFE8E8FF),
    onSurface        = Color(0xFFE8E8FF),
    error            = ErrorRed,
    tertiary         = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary          = Purple40,
    onPrimary        = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    secondary        = PurpleGrey40,
    background       = LightBackground,
    surface          = LightSurface,
    surfaceVariant   = Color(0xFFF3F0FF),
    onBackground     = Color(0xFF1A1A2E),
    onSurface        = Color(0xFF1A1A2E),
    error            = ErrorRed,
    tertiary         = Pink40
)

@Composable
fun PocketBrainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}
