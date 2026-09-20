package com.priyanshusingh.focusplanbuilder.model

/**
 * Classifies a study duration, in minutes, into the category label shown on
 * the result card.
 *
 * | Duration           | Category             |
 * |--------------------|----------------------|
 * | less than 10       | "Invalid"            |
 * | 10–29 minutes      | "Quick review"       |
 * | 30–60 minutes      | "Focused session"    |
 * | more than 60       | "Extended session"   |
 *
 * The function is total: it returns a sensible label for *every* Int, including
 * zero, negatives, and values above the UI's 180-minute cap. The UI never
 * calls it with anything outside 10..180 (the button is disabled otherwise),
 * but a total function is safer to test and to reuse.
 *
 * ```
 * durationCategory(20)   // "Quick review"
 * durationCategory(45)   // "Focused session"
 * durationCategory(90)   // "Extended session"
 * ```
 */
fun durationCategory(minutes: Int): String = when {
    minutes < MIN_MINUTES -> CATEGORY_INVALID
    minutes in MIN_MINUTES..QUICK_REVIEW_MAX_MINUTES -> CATEGORY_QUICK_REVIEW
    minutes in (QUICK_REVIEW_MAX_MINUTES + 1)..FOCUSED_SESSION_MAX_MINUTES -> CATEGORY_FOCUSED_SESSION
    else -> CATEGORY_EXTENDED_SESSION
}

/**
 * True when [category] is one of the three labels a real plan can carry.
 * "Invalid" is deliberately excluded.
 */
fun isPlanCategory(category: String): Boolean =
    category == CATEGORY_QUICK_REVIEW ||
        category == CATEGORY_FOCUSED_SESSION ||
        category == CATEGORY_EXTENDED_SESSION
