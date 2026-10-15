package com.priyanshusingh.campuseventexplorer.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlin.math.abs

/**
 * A container / on-container colour pair used to tint everything that represents one category:
 * the badge on a card, the quick-filter chip and the accent stripe on the card edge.
 */
@Immutable
data class CategoryAccent(
    val container: Color,
    val onContainer: Color,
)

/**
 * Picks a stable accent for [category] from the Material 3 tonal containers of the current
 * theme (primary / secondary / tertiary / error / surface tints).
 *
 * The choice is a pure function of the category name, so the same category always gets the
 * same colour, and because only `colorScheme` roles are used the result adapts automatically to
 * dark mode and to Material You dynamic colour.
 */
@Composable
fun categoryAccent(category: String): CategoryAccent {
    val scheme = MaterialTheme.colorScheme
    val palette = listOf(
        CategoryAccent(scheme.primaryContainer, scheme.onPrimaryContainer),
        CategoryAccent(scheme.tertiaryContainer, scheme.onTertiaryContainer),
        CategoryAccent(scheme.secondaryContainer, scheme.onSecondaryContainer),
        CategoryAccent(scheme.errorContainer, scheme.onErrorContainer),
        CategoryAccent(scheme.surfaceVariant, scheme.onSurfaceVariant),
    )
    val index = abs(category.lowercase().hashCode()) % palette.size
    return palette[index]
}
