package com.priyanshusingh.focusplanbuilder.model

/**
 * Single source of truth for every number and label the plan logic depends on.
 *
 * Keeping these in one file means the validation code, the two calculation
 * functions, the UI hints, and the unit tests can never disagree about where a
 * boundary sits. Change a value here and everything else follows.
 */

/** Smallest study duration the app accepts, inclusive. */
const val MIN_MINUTES: Int = 10

/** Largest study duration the app accepts, inclusive. */
const val MAX_MINUTES: Int = 180

/** The full accepted window, expressed as a range for `in` checks. */
val VALID_MINUTES_RANGE: IntRange = MIN_MINUTES..MAX_MINUTES

/** Last minute that still counts as a "Quick review" (10–29). */
const val QUICK_REVIEW_MAX_MINUTES: Int = 29

/** Last minute that still counts as a "Focused session" (30–60). */
const val FOCUSED_SESSION_MAX_MINUTES: Int = 60

/** Category labels exactly as the assignment specifies them. */
const val CATEGORY_INVALID: String = "Invalid"
const val CATEGORY_QUICK_REVIEW: String = "Quick review"
const val CATEGORY_FOCUSED_SESSION: String = "Focused session"
const val CATEGORY_EXTENDED_SESSION: String = "Extended session"

/** Recommended break lengths, in minutes, per category. */
const val BREAK_QUICK_REVIEW: Int = 5
const val BREAK_FOCUSED_SESSION: Int = 10
const val BREAK_EXTENDED_SESSION: Int = 15

/**
 * Break returned for durations the UI never lets through (below [MIN_MINUTES]).
 * Zero reads as "no break applies" and keeps [recommendedBreak] total, i.e.
 * defined for every Int, so it can never throw.
 */
const val BREAK_NOT_APPLICABLE: Int = 0

/** Preset durations offered as one-tap chips under the minutes field. */
val QUICK_PICK_MINUTES: List<Int> = listOf(15, 25, 45, 60, 90, 120)
