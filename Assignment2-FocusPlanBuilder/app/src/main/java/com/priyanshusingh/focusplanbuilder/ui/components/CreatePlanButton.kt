package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.MinutesValidation
import com.priyanshusingh.focusplanbuilder.model.isSubjectValid
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import com.priyanshusingh.focusplanbuilder.ui.theme.FieldShape

/**
 * The single primary action on the screen.
 *
 * [enabled] is handed in, never computed here. This component has no idea what
 * makes a plan valid, and it should not: the rule lives in FocusPlanRoute as
 * `subject.isNotBlank() && minutes != null && minutes in 10..180`, evaluated
 * fresh on every recomposition, with no separate Boolean state to drift.
 */
@Composable
fun CreatePlanButton(
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = FieldShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .testTag(FocusPlanTestTags.CREATE_BUTTON)
    ) {
        Text(
            text = "Create plan",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

/**
 * One quiet line under the button that says exactly what is still missing, so
 * a disabled button is never a mystery. Derived entirely from the inputs.
 */
@Composable
fun PlanReadinessHint(
    subject: String,
    minutesValidation: MinutesValidation,
    canCreatePlan: Boolean,
    modifier: Modifier = Modifier
) {
    val message = when {
        canCreatePlan -> "Ready. Tap Create plan to build your session."
        !isSubjectValid(subject) && minutesValidation is MinutesValidation.Valid ->
            "Add a subject to enable the button."
        !isSubjectValid(subject) -> "Add a subject and a duration to get started."
        minutesValidation is MinutesValidation.Empty -> "Now enter how many minutes you have."
        else -> "Fix the minutes field to enable the button."
    }

    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = if (canCreatePlan) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .testTag(FocusPlanTestTags.READINESS_HINT)
    )
}
