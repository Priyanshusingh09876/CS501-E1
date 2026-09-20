package com.priyanshusingh.focusplanbuilder.model

/**
 * Classifies a study duration, in minutes, into a human-readable category.
 *
 * Duration        Category
 * < 10            "Invalid"
 * 10-29           "Quick review"
 * 30-60           "Focused session"
 * > 60            "Extended session"
 *
 * Deliberately defined over the *entire* Int domain -- including zero,
 * negative numbers, and values far above 180 -- rather than only the
 * 10..180 window the UI itself allows through. This means the function
 * behaves predictably (never throws, never returns a nonsensical result)
 * even if called directly by a test, or by some future caller that does
 * not route through the UI's own validation first.
 */
fun durationCategory(minutes: Int): String = when {
    minutes < 10 -> "Invalid"
    minutes in 10..29 -> "Quick review"
    minutes in 30..60 -> "Focused session"
    else -> "Extended session" // covers everything above 60, no upper bound
}
