package com.priyanshusingh.focusplanbuilder.model

/**
 * Builds the human-readable summary sentence shown on the result card.
 *
 * Example: "Study Compose State for 45 minutes, and then take a
 * 10-minute break."
 *
 * Kept as a standalone function (rather than inline string interpolation
 * inside the Composable) so the exact sentence format is unit-testable
 * without needing to render any UI.
 */
fun buildSummary(plan: FocusPlan): String =
    "Study ${plan.subject} for ${plan.minutes} minutes, and then take a " +
        "${plan.breakMinutes}-minute break."
