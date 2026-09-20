package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Indigo40,
    onPrimary = Color.White,
    primaryContainer = Indigo90,
    onPrimaryContainer = Indigo20,
    secondary = Indigo60,
    onSecondary = Color.White,
    secondaryContainer = Indigo95,
    onSecondaryContainer = Indigo20,
    tertiary = Amber40,
    onTertiary = Color.White,
    tertiaryContainer = Amber90,
    onTertiaryContainer = Amber20,
    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error20,
    background = Parchment,
    onBackground = Ink,
    surface = ParchmentSurface,
    onSurface = Ink,
    surfaceVariant = Sand20,
    onSurfaceVariant = InkMuted,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Sand10,
    surfaceContainer = Sand20,
    surfaceContainerHigh = Sand30,
    surfaceContainerHighest = Sand40,
    outline = InkFaint,
    outlineVariant = Sand40,
    inverseSurface = Indigo20,
    inverseOnSurface = Cloud,
    inversePrimary = Indigo80
)

private val DarkColors = darkColorScheme(
    primary = Indigo80,
    onPrimary = Indigo20,
    primaryContainer = Indigo30,
    onPrimaryContainer = Indigo90,
    secondary = Indigo60,
    onSecondary = Indigo10,
    secondaryContainer = Night40,
    onSecondaryContainer = Indigo90,
    tertiary = Amber70,
    onTertiary = Amber20,
    tertiaryContainer = Amber30,
    onTertiaryContainer = Amber90,
    error = Error80,
    onError = Error20,
    errorContainer = Error20,
    onErrorContainer = Error90,
    background = Night,
    onBackground = Cloud,
    surface = Night,
    onSurface = Cloud,
    surfaceVariant = Night30,
    onSurfaceVariant = CloudMuted,
    surfaceContainerLowest = Indigo10,
    surfaceContainerLow = Night10,
    surfaceContainer = Night20,
    surfaceContainerHigh = Night30,
    surfaceContainerHighest = Night40,
    outline = Night80,
    outlineVariant = Night60,
    inverseSurface = Cloud,
    inverseOnSurface = Night,
    inversePrimary = Indigo40
)

/**
 * The app's Material 3 theme.
 *
 * Dynamic (wallpaper-based) color is intentionally not used: it would replace
 * the deliberately chosen indigo/amber/parchment palette with whatever tint the
 * device wallpaper happens to have. Light and dark mode still follow the system
 * setting. Status/navigation bar colors are handled by `enableEdgeToEdge()` in
 * MainActivity, which is the modern, non-deprecated approach.
 */
@Composable
fun FocusPlanBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = FocusTypography,
        shapes = FocusShapes,
        content = content
    )
}
