package com.priyanshusingh.focusplanbuilder.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Returns the accent color associated with a duration category string
 * (as produced by model/DurationCategoryCalculator.kt's durationCategory()).
 *
 * Kept in the ui.theme package rather than the model package: the model
 * layer is plain Kotlin with no Android/Compose dependency at all, and a
 * Color is a Compose UI concept, not a business rule. This function is the
 * seam between "what category is this" (model) and "what color represents
 * that category" (presentation).
 */
fun categoryAccentColor(category: String): Color = when (category) {
    "Quick review" -> CategoryQuickReview
    "Focused session" -> CategoryFocusedSession
    "Extended session" -> CategoryExtendedSession
    else -> FocusInk // "Invalid" or any unexpected value; should not occur
    // in practice since the UI never creates a plan for an invalid duration.
}
