package com.priyanshu.mobilitylens.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Navy700,
    onPrimary = Surface,
    primaryContainer = Navy100,
    onPrimaryContainer = Navy900,
    secondary = Amber400,
    onSecondary = Navy900,
    secondaryContainer = Amber200,
    onSecondaryContainer = Navy900,
    background = Surface,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    error = ErrorRed,
    onError = OnError
)

private val DarkColorScheme = darkColorScheme(
    primary = Navy300,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = Navy100,
    secondary = Amber400,
    onSecondary = Navy900,
    background = Navy900,
    onBackground = Surface,
    surface = Navy900,
    onSurface = Surface,
    surfaceVariant = Navy700,
    onSurfaceVariant = Navy100,
    error = ErrorRed,
    onError = OnError
)

@Composable
fun MobilityLensTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
