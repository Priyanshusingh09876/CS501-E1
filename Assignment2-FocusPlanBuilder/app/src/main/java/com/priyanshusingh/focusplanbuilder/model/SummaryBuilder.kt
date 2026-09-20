package com.priyanshusingh.focusplanbuilder.model

/**
 * The complete summary sentence shown at the bottom of the result card.
 *
 * Example: "Study Compose State for 45 minutes, and then take a 10-minute break."
 *
 * Kept out of the composable so the exact wording is covered by plain unit
 * tests; the card only renders whatever this returns.
 */
fun buildSummary(plan: FocusPlan): String =
    "Study ${plan.subject} for ${plan.minutes} minutes, " +
        "and then take a ${plan.breakMinutes}-minute break."

/** "Duration: 45 minutes" */
fun durationLine(plan: FocusPlan): String = "Duration: ${plan.minutes} minutes"

/** "Category: Focused session" */
fun categoryLine(plan: FocusPlan): String = "Category: ${plan.category}"

/** "Recommended break: 10 minutes" */
fun breakLine(plan: FocusPlan): String = "Recommended break: ${plan.breakMinutes} minutes"

/**
 * A short live preview shown under the minutes field *before* the plan is
 * created, e.g. "45 min → Focused session · 10-minute break". Returns null when
 * the minutes are not valid yet so the field can fall back to its default hint.
 */
fun minutesPreview(minutes: Int?): String? {
    if (minutes == null || minutes !in VALID_MINUTES_RANGE) return null
    return "$minutes min → ${durationCategory(minutes)} · ${recommendedBreak(minutes)}-minute break"
}
