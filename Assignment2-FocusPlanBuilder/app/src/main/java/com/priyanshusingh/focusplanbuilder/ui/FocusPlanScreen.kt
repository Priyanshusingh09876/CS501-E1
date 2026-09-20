package com.priyanshusingh.focusplanbuilder.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.ui.components.CreatePlanButton
import com.priyanshusingh.focusplanbuilder.ui.components.FocusPlanInstructions
import com.priyanshusingh.focusplanbuilder.ui.components.FocusPlanResultCard
import com.priyanshusingh.focusplanbuilder.ui.components.FocusPlanTitle
import com.priyanshusingh.focusplanbuilder.ui.components.MinutesInputField
import com.priyanshusingh.focusplanbuilder.ui.components.SubjectInputField

/**
 * FocusPlanScreen is intentionally "dumb": it never owns [subject] or
 * [minutesText] itself, never decides validity, and never constructs a
 * FocusPlan. It only:
 *   - lays out whatever values it is given, by delegating to small
 *     single-purpose composables in ui/components/, and
 *   - reports user actions upward through callbacks.
 *
 * This separation (state hoisting) is what lets FocusPlanRoute be tested,
 * reused, or swapped independently of the visual layer, and is required by
 * the assignment. Each visual piece (title, instructions, the two input
 * fields, the button, the result card) lives in its own file under
 * ui/components/ so this file stays a short, readable "table of contents"
 * for the screen rather than one long block of layout code.
 *
 * Content is centered and capped at 480dp wide so the layout stays
 * comfortable to read on a tablet or a rotated phone rather than
 * stretching input fields edge to edge.
 */
@Composable
fun FocusPlanScreen(
    subject: String,
    minutesText: String,
    plan: FocusPlan?,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    canCreatePlan: Boolean,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    // The result card animates in when a plan is created, and out when it
    // is cleared -- the one deliberate motion moment on this screen,
    // triggered directly by the user's own action (pressing the button, or
    // editing a field afterward). AnimatedVisibility needs *something* to
    // keep rendering while it fades/shrinks out, but `plan` itself has
    // already gone back to null by that point (see FocusPlanRoute's reset
    // behavior) -- so the last real plan is cached here, purely for the
    // exit animation to have content to show. This is local display state
    // only; it never feeds back into canCreatePlan or any other logic.
    var lastPlan by remember { mutableStateOf<FocusPlan?>(null) }
    LaunchedEffect(plan) {
        if (plan != null) lastPlan = plan
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.widthIn(max = 480.dp),
            verticalArrangement = Arrangement.Top
        ) {
            FocusPlanTitle()

            Spacer(modifier = Modifier.height(8.dp))

            FocusPlanInstructions()

            Spacer(modifier = Modifier.height(28.dp))

            SubjectInputField(
                value = subject,
                onValueChange = onSubjectChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            MinutesInputField(
                value = minutesText,
                onValueChange = onMinutesChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            CreatePlanButton(
                enabled = canCreatePlan,
                onClick = onCreatePlan
            )

            Spacer(modifier = Modifier.height(24.dp))

            // The result card is only actually present in the plan-holding
            // state when plan is non-null -- per requirement 9, it must not
            // be visible before the first plan is created, and per
            // requirement 12, it must go away as soon as either input
            // changes (FocusPlanRoute sets plan back to null then).
            AnimatedVisibility(
                visible = plan != null,
                enter = fadeIn(tween(220)) + expandVertically(tween(220)),
                exit = fadeOut(tween(140)) + shrinkVertically(tween(140))
            ) {
                lastPlan?.let { FocusPlanResultCard(plan = it) }
            }
        }
    }
}
