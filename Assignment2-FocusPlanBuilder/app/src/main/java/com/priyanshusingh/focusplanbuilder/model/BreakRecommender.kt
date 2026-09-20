package com.priyanshusingh.focusplanbuilder.model

/**
 * Recommends a break length, in minutes, for a study session of [minutes].
 *
 * | Study duration     | Recommended break |
 * |--------------------|-------------------|
 * | 10–29 minutes      | 5 minutes         |
 * | 30–60 minutes      | 10 minutes        |
 * | more than 60       | 15 minutes        |
 *
 * Durations under 10 minutes are never valid in the UI, so they return
 * [BREAK_NOT_APPLICABLE] (0) instead of throwing. Like [durationCategory],
 * this is a total function over Int, and the two functions share the same
 * band boundaries so they can never disagree about which band a value is in.
 */
fun recommendedBreak(minutes: Int): Int = when {
    minutes < MIN_MINUTES -> BREAK_NOT_APPLICABLE
    minutes in MIN_MINUTES..QUICK_REVIEW_MAX_MINUTES -> BREAK_QUICK_REVIEW
    minutes in (QUICK_REVIEW_MAX_MINUTES + 1)..FOCUSED_SESSION_MAX_MINUTES -> BREAK_FOCUSED_SESSION
    else -> BREAK_EXTENDED_SESSION
}
