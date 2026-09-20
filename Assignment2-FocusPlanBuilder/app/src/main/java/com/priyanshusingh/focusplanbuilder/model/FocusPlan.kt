package com.priyanshusingh.focusplanbuilder.model

/**
 * Represents one completed, validated study plan.
 *
 * This class intentionally holds only already-validated data:
 * - [subject] is guaranteed non-blank (and trimmed) by the time a FocusPlan exists.
 * - [minutes] is guaranteed to be an Int in 10..180 by the time a FocusPlan exists.
 *
 * All validation happens *before* a FocusPlan is created (see FocusPlanRoute.kt),
 * so this data class never needs to defend against invalid state itself.
 */
data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)
