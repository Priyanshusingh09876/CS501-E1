package com.priyanshusingh.focusplanbuilder.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.isSubjectValid
import com.priyanshusingh.focusplanbuilder.model.parseValidMinutes
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak

/**
 * FocusPlanRoute is the single source of truth for this screen's state.
 *
 * Responsibilities (and ONLY these — presentation lives in FocusPlanScreen):
 *  1. Own [subject], [minutesText], and the last created [plan].
 *  2. Validate the current input on every recomposition.
 *  3. Respond to user events (text changes, button click).
 *  4. Construct a FocusPlan from validated input.
 *  5. Hand plain values + callbacks down to the stateless FocusPlanScreen.
 *
 * Because [subject] and [minutesText] are declared with rememberSaveable,
 * both fields survive configuration changes such as device rotation. The
 * generated [plan], by contrast, uses plain `remember` — the assignment
 * says preserving the plan across rotation is optional, and recomputing it
 * from saved input (if desired) is trivial, so we keep this simpler.
 */
@Composable
fun FocusPlanRoute(
    modifier: Modifier = Modifier
) {
    // rememberSaveable stores these values in the Activity's saved-instance
    // state (via a Bundle), not just in memory. A plain `remember` would be
    // wiped out when the Activity is destroyed and recreated (e.g. on
    // rotation); rememberSaveable survives that because Compose serializes
    // the value into the Bundle and restores it afterwards.
    var subject by rememberSaveable { mutableStateOf("") }
    var minutesText by rememberSaveable { mutableStateOf("") }

    // The generated plan is transient UI state, not user input, so a plain
    // `remember` is sufficient. Losing it on rotation only means the user
    // would need to tap "Create plan" again, which is an acceptable and
    // explicitly optional trade-off per the assignment.
    var plan by remember { mutableStateOf<FocusPlan?>(null) }

    // canCreatePlan is NOT a separate mutable Boolean stored in state.
    // It is recalculated from `subject` and `minutesText` on every
    // recomposition, so it can never drift out of sync with the actual
    // input. toIntOrNull() (inside parseValidMinutes) is used instead of
    // toInt() so that non-numeric text such as "abc" or a blank string
    // produces null instead of throwing a NumberFormatException — this is
    // what keeps typing garbage into the minutes field from crashing the app.
    val validatedMinutes = parseValidMinutes(minutesText)
    val canCreatePlan = isSubjectValid(subject) && validatedMinutes != null

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        plan = plan,
        canCreatePlan = canCreatePlan,
        onSubjectChange = { newSubject ->
            subject = newSubject
            // Reset behavior: any edit to an input field invalidates the
            // previously displayed result. The old card must not linger
            // while the user is composing a new plan.
            plan = null
        },
        onMinutesChange = { newMinutesText ->
            minutesText = newMinutesText
            plan = null
        },
        onCreatePlan = {
            // Re-validate defensively at the moment of the click rather than
            // trusting canCreatePlan blindly. In normal operation the button
            // is disabled unless canCreatePlan is true, so this branch is
            // always taken when the button is actually clickable — but
            // guarding here means this lambda is safe to call from anywhere
            // (e.g. a future "create on Enter key" handler) without risking
            // a crash or an invalid FocusPlan ever being constructed.
            val minutes = parseValidMinutes(minutesText)
            if (isSubjectValid(subject) && minutes != null) {
                val cleanedSubject = subject.trim()
                plan = FocusPlan(
                    subject = cleanedSubject,
                    minutes = minutes,
                    category = durationCategory(minutes),
                    breakMinutes = recommendedBreak(minutes)
                )
            }
        },
        modifier = modifier
    )
}
