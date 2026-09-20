package com.priyanshusingh.focusplanbuilder.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.MAX_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MIN_MINUTES
import com.priyanshusingh.focusplanbuilder.model.cleanSubject
import com.priyanshusingh.focusplanbuilder.model.durationCategory
import com.priyanshusingh.focusplanbuilder.model.recommendedBreak
import com.priyanshusingh.focusplanbuilder.model.subjectErrorMessage
import com.priyanshusingh.focusplanbuilder.model.validateMinutes

/**
 * The stateful half of the screen and the single source of truth for it.
 *
 * FocusPlanRoute, and only FocusPlanRoute:
 *  1. **Owns the state** — [subject], [minutesText], and the generated [plan].
 *  2. **Validates the input** — recomputes `canCreatePlan` on every recomposition.
 *  3. **Responds to events** — the three callbacks below mutate state.
 *  4. **Creates the FocusPlan** — from validated input and the two calculation functions.
 *  5. **Passes values and callbacks down** to the stateless [FocusPlanScreen].
 *
 * Nothing here draws anything; nothing in [FocusPlanScreen] owns anything.
 */
@Composable
fun FocusPlanRoute(
    modifier: Modifier = Modifier
) {
    // rememberSaveable writes these into the Activity's saved-instance-state
    // Bundle, so they come back after rotation (or any other configuration
    // change that recreates the Activity). A plain `remember` lives only in
    // the in-memory composition and would be wiped along with it.
    var subject by rememberSaveable {
        mutableStateOf("")
    }

    var minutesText by rememberSaveable {
        mutableStateOf("")
    }

    // Preserving the plan is optional per the assignment, but it is cheap with
    // a custom Saver and means the result card also survives rotation.
    var plan: FocusPlan? by rememberSaveable(stateSaver = FocusPlanSaver) {
        mutableStateOf(null)
    }

    // --- Validation: derived on every recomposition, never stored -----------
    //
    // toIntOrNull() is the whole crash-safety story: "", "abc", "10.5" or a
    // 30-digit number all become null instead of a NumberFormatException.
    val minutes: Int? = minutesText.toIntOrNull()

    // Not a separate mutable Boolean. Because it is computed from the two text
    // states, any keystroke that changes either one recomposes this call site
    // and the button's `enabled` follows automatically.
    val canCreatePlan =
        subject.isNotBlank() &&
            minutes != null &&
            minutes in MIN_MINUTES..MAX_MINUTES

    // Richer, user-facing detail for the fields' supporting text.
    val minutesValidation = validateMinutes(minutesText)
    val subjectError = subjectErrorMessage(subject)

    FocusPlanScreen(
        subject = subject,
        minutesText = minutesText,
        plan = plan,
        onSubjectChange = { newSubject ->
            // Reset behaviour (requirement 12): editing an input invalidates the
            // plan that was built from the previous input. The guard keeps a
            // no-op change (same text re-delivered by the IME) from hiding the card.
            if (newSubject != subject) {
                subject = newSubject
                plan = null
            }
        },
        onMinutesChange = { newMinutesText ->
            if (newMinutesText != minutesText) {
                minutesText = newMinutesText
                plan = null
            }
        },
        canCreatePlan = canCreatePlan,
        onCreatePlan = {
            // Defensive re-validation at click time. The button is disabled
            // unless canCreatePlan is true, but this callback is also reachable
            // from the keyboard's "Done" action, so it never trusts the caller.
            val validatedMinutes: Int? = minutesText.toIntOrNull()
            if (subject.isNotBlank() &&
                validatedMinutes != null &&
                validatedMinutes in MIN_MINUTES..MAX_MINUTES
            ) {
                plan = FocusPlan(
                    subject = cleanSubject(subject),
                    minutes = validatedMinutes,
                    category = durationCategory(validatedMinutes),
                    breakMinutes = recommendedBreak(validatedMinutes)
                )
            }
        },
        onStartOver = {
            subject = ""
            minutesText = ""
            plan = null
        },
        minutesValidation = minutesValidation,
        subjectError = subjectError,
        modifier = modifier
    )
}
