package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.MinutesValidation
import com.priyanshusingh.focusplanbuilder.ui.theme.PanelShape

/**
 * The tinted panel that groups everything the user edits: both text fields,
 * the quick-pick chips, and the duration scale.
 *
 * It is a pure pass-through. Every value comes in as a parameter and every
 * change goes back out through a callback; nothing in here is remembered.
 */
@Composable
fun PlanInputPanel(
    subject: String,
    minutesText: String,
    subjectError: String?,
    minutesValidation: MinutesValidation,
    canCreatePlan: Boolean,
    onSubjectChange: (String) -> Unit,
    onMinutesChange: (String) -> Unit,
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = PanelShape,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        tonalElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp)) {
            SectionLabel(step = "01", title = "Subject")

            Spacer(modifier = Modifier.height(10.dp))

            SubjectInputField(
                value = subject,
                onValueChange = onSubjectChange,
                errorMessage = subjectError
            )

            Spacer(modifier = Modifier.height(14.dp))

            SectionLabel(step = "02", title = "Time available")

            Spacer(modifier = Modifier.height(10.dp))

            MinutesInputField(
                value = minutesText,
                onValueChange = onMinutesChange,
                validation = minutesValidation,
                canCreatePlan = canCreatePlan,
                onCreatePlan = onCreatePlan
            )

            Spacer(modifier = Modifier.height(10.dp))

            QuickPickChips(
                minutesText = minutesText,
                onMinutesChange = onMinutesChange
            )

            Spacer(modifier = Modifier.height(18.dp))

            DurationScale(
                minutes = (minutesValidation as? MinutesValidation.Valid)?.minutes
            )
        }
    }
}

/** "01  Subject" style eyebrow above each input group. */
@Composable
private fun SectionLabel(step: String, title: String) {
    Text(
        text = "$step  ·  $title",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
