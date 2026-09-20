package com.priyanshusingh.focusplanbuilder.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = FocusIndigo,
    onPrimary = Color.White,
    secondary = FocusAmber,
    onSecondary = FocusInk,
    tertiary = CategoryExtendedSession,
    background = FocusParchment,
    onBackground = FocusInk,
    surface = FocusParchment,
    onSurface = FocusInk,
    surfaceVariant = FocusSurfaceLight,
    onSurfaceVariant = FocusInk
)

private val DarkColors = darkColorScheme(
    primary = FocusAmber,
    onPrimary = FocusInk,
    secondary = FocusIndigo,
    onSecondary = Color.White,
    tertiary = CategoryQuickReview,
    background = FocusIndigoDark,
    onBackground = FocusParchment,
    surface = FocusIndigoDark,
    onSurface = FocusParchment,
    surfaceVariant = FocusSurfaceDark,
    onSurfaceVariant = FocusParchment
)

/**
 * This app's Material 3 theme.
 *
 * Unlike a freshly generated Compose template, this does NOT default to
 * Android 12+ dynamic color: dynamic color would replace the deliberately
 * chosen indigo/amber/parchment palette above with whatever tint happens
 * to come from the user's wallpaper, which defeats the point of choosing a
 * specific identity for this app. The custom [LightColors]/[DarkColors]
 * schemes are used unconditionally; only light/dark mode still follows the
 * system setting.
 */
@Composable
fun FocusPlanBuilderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
