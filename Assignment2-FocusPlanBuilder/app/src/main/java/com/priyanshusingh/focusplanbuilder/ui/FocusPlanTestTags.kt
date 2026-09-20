package com.priyanshusingh.focusplanbuilder.ui

/**
 * Stable semantics tags for the instrumented UI tests.
 *
 * Tests find nodes by tag rather than by visible text so that the same string
 * appearing twice on screen (for example the subject typed into the field and
 * the subject shown on the card) never makes a lookup ambiguous, and so that
 * copy changes do not silently break the tests.
 */
object FocusPlanTestTags {
    const val SCREEN = "focus_plan_screen"
    const val SUBJECT_FIELD = "subject_field"
    const val MINUTES_FIELD = "minutes_field"
    const val CREATE_BUTTON = "create_plan_button"
    const val READINESS_HINT = "readiness_hint"
    const val RESULT_CARD = "result_card"
    const val RESULT_SUBJECT = "result_subject"
    const val RESULT_DURATION = "result_duration"
    const val RESULT_CATEGORY = "result_category"
    const val RESULT_BREAK = "result_break"
    const val RESULT_SUMMARY = "result_summary"
    const val START_OVER_BUTTON = "start_over_button"
    const val QUICK_PICK_PREFIX = "quick_pick_"
}
