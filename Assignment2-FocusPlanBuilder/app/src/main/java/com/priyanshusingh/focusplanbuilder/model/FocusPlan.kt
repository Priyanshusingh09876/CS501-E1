package com.priyanshusingh.focusplanbuilder.model

/**
 * One completed, validated study plan.
 *
 * A FocusPlan only ever exists *after* validation has passed, so its fields
 * carry guarantees the rest of the app can rely on:
 *  - [subject] is non-blank and already cleaned (see [cleanSubject]).
 *  - [minutes] is an Int in 10..180.
 *  - [category] is the result of [durationCategory] for [minutes].
 *  - [breakMinutes] is the result of [recommendedBreak] for [minutes].
 *
 * Use [createFocusPlan] rather than the constructor when starting from raw
 * user input; the constructor is kept public because the assignment specifies
 * this exact data class and the tests construct it directly.
 */
data class FocusPlan(
    val subject: String,
    val minutes: Int,
    val category: String,
    val breakMinutes: Int
)

/** Study time plus the recommended break: the whole block the plan occupies. */
val FocusPlan.totalMinutes: Int
    get() = minutes + breakMinutes

/** Share of the whole block spent studying, in 0f..1f, for proportional bars. */
val FocusPlan.studyFraction: Float
    get() = if (totalMinutes == 0) 0f else minutes.toFloat() / totalMinutes

/** The band this plan's category belongs to, or null if the label is unknown. */
val FocusPlan.band: DurationBand?
    get() = DurationBand.fromCategory(category)
