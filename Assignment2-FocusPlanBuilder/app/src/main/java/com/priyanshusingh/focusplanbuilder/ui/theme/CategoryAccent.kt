package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.priyanshusingh.focusplanbuilder.model.DurationBand

/**
 * The trio of colors used to paint one duration category: a strong [accent]
 * for stripes, bars and badges, a soft [container] for tinted backgrounds, and
 * the matching [onContainer] text color.
 */
data class CategoryColors(
    val accent: Color,
    val container: Color,
    val onContainer: Color
)

/**
 * Colors for a [DurationBand], resolved for the current light/dark theme.
 *
 * This lives in the theme package, not the model: the model is plain Kotlin
 * with no Compose dependency, and "which color represents a category" is a
 * presentation decision, not a business rule.
 */
@Composable
fun categoryColors(band: DurationBand?): CategoryColors {
    val dark = isSystemInDarkTheme()
    return when (band) {
        DurationBand.QUICK_REVIEW -> CategoryColors(
            accent = CoralAccent,
            container = if (dark) CoralContainerDark else CoralContainerLight,
            onContainer = if (dark) Color(0xFFFFD9C7) else Color(0xFF5A2A14)
        )
        DurationBand.FOCUSED_SESSION -> CategoryColors(
            accent = if (dark) Indigo80 else FocusAccent,
            container = if (dark) FocusContainerDark else FocusContainerLight,
            onContainer = if (dark) Indigo90 else Indigo20
        )
        DurationBand.EXTENDED_SESSION -> CategoryColors(
            accent = if (dark) Color(0xFFC9A8DA) else PlumAccent,
            container = if (dark) PlumContainerDark else PlumContainerLight,
            onContainer = if (dark) Color(0xFFEDE3F2) else Color(0xFF3B2A45)
        )
        null -> CategoryColors(
            accent = MaterialTheme.colorScheme.outline,
            container = MaterialTheme.colorScheme.surfaceContainerHigh,
            onContainer = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Convenience overload keyed by the category label a [com.priyanshusingh.focusplanbuilder.model.FocusPlan] carries. */
@Composable
fun categoryColors(category: String): CategoryColors =
    categoryColors(DurationBand.fromCategory(category))
