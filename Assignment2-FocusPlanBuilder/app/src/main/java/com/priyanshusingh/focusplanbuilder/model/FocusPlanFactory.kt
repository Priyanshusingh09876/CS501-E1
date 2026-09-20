package com.priyanshusingh.focusplanbuilder.model

/**
 * Turns raw user input into a [FocusPlan], or null when the input is not valid.
 *
 * This is the one place where the four pieces of plan logic meet:
 *  1. [isSubjectValid] and [cleanSubject] for the subject,
 *  2. [parseValidMinutes] (built on `toIntOrNull()`) for the duration,
 *  3. [durationCategory] for the category label,
 *  4. [recommendedBreak] for the break length.
 *
 * Because it re-validates from scratch, it is safe to call from anywhere: the
 * button click, a keyboard "Done" action, or a unit test. It can never build a
 * plan from invalid input and it can never throw on bad text.
 */
fun createFocusPlan(subject: String, minutesText: String): FocusPlan? {
    if (!isSubjectValid(subject)) return null
    val minutes: Int = parseValidMinutes(minutesText) ?: return null

    return FocusPlan(
        subject = cleanSubject(subject),
        minutes = minutes,
        category = durationCategory(minutes),
        breakMinutes = recommendedBreak(minutes)
    )
}

/**
 * Mirrors the button-enabled rule from the screen so tests can assert it
 * without Compose:
 *
 * ```
 * subject.isNotBlank() && minutes != null && minutes in 10..180
 * ```
 */
fun canCreatePlan(subject: String, minutesText: String): Boolean {
    val minutes: Int? = minutesText.toIntOrNull()
    return subject.isNotBlank() && minutes != null && minutes in VALID_MINUTES_RANGE
}
