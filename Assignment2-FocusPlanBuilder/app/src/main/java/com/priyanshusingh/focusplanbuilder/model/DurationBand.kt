package com.priyanshusingh.focusplanbuilder.model

/**
 * The three valid duration bands, described as data so the UI can render a
 * scale, a legend, and a one-line tip for each band without hard-coding the
 * numbers a second time.
 *
 * [durationCategory] and [recommendedBreak] remain the functions of record
 * (the assignment asks for them by signature). This enum mirrors them and is
 * cross-checked against them in `DurationBandTest`, so the two views of the
 * same rule can never drift apart.
 */
enum class DurationBand(
    val label: String,
    val shortLabel: String,
    val range: IntRange,
    val breakMinutes: Int,
    val tip: String
) {
    QUICK_REVIEW(
        label = CATEGORY_QUICK_REVIEW,
        shortLabel = "Quick",
        range = MIN_MINUTES..QUICK_REVIEW_MAX_MINUTES,
        breakMinutes = BREAK_QUICK_REVIEW,
        tip = "Great for flashcards, skimming notes, or a fast recap."
    ),
    FOCUSED_SESSION(
        label = CATEGORY_FOCUSED_SESSION,
        shortLabel = "Focused",
        range = (QUICK_REVIEW_MAX_MINUTES + 1)..FOCUSED_SESSION_MAX_MINUTES,
        breakMinutes = BREAK_FOCUSED_SESSION,
        tip = "One topic, phone away, no tab switching."
    ),
    EXTENDED_SESSION(
        label = CATEGORY_EXTENDED_SESSION,
        shortLabel = "Extended",
        range = (FOCUSED_SESSION_MAX_MINUTES + 1)..MAX_MINUTES,
        breakMinutes = BREAK_EXTENDED_SESSION,
        tip = "Deep work. Stand up and stretch during the break."
    );

    /** Human-readable range such as "10–29 min". */
    val rangeLabel: String
        get() = "${range.first}–${range.last} min"

    companion object {
        /**
         * The band containing [minutes], or null when the value is outside the
         * accepted 10..180 window.
         */
        fun fromMinutes(minutes: Int): DurationBand? =
            entries.firstOrNull { minutes in it.range }

        /**
         * The band whose label equals [category], or null for "Invalid" and any
         * unexpected string.
         */
        fun fromCategory(category: String): DurationBand? =
            entries.firstOrNull { it.label == category }
    }
}
