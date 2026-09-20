package com.priyanshusingh.focusplanbuilder.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.MinutesValidation
import com.priyanshusingh.focusplanbuilder.model.subjectErrorMessage
import com.priyanshusingh.focusplanbuilder.model.validateMinutes
import com.priyanshusingh.focusplanbuilder.ui.components.CreatePlanButton
import com.priyanshusingh.focusplanbuilder.ui.components.FocusPlanHeader
import com.priyanshusingh.focusplanbuilder.ui.components.FocusPlanResultCard
import com.priyanshusingh.focusplanbuilder.ui.components.PlanInputPanel
import com.priyanshusingh.focusplanbuilder.ui.components.PlanReadinessHint
import com.priyanshusingh.focusplanbuilder.ui.components.ResultPlaceholder
import com.priyanshusingh.focusplanbuilder.ui.theme.FocusPlanBuilderTheme

/**
 * The stateless half of the screen.
 *
 * FocusPlanScreen never owns [subject], [minutesText] or [plan]; it receives
 * them as plain parameters, lays them out, and reports every user action back
 * up through the callbacks. That separation (state hoisting) is what makes this
 * composable trivially previewable and testable: hand it values, look at pixels.
 *
 * Each visual section lives in its own file under `ui/components/` so this
 * function reads as a table of contents for the screen.
 *
 * @param minutesValidation Detailed validation state for the minutes field,
 *   used only for the helper/error text. Defaults to recomputing it, so callers
 *   that do not care (like previews) can omit it.
 * @param subjectError Error text for the subject field, or null.
 * @param onStartOver Clears both inputs and the plan.
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
    modifier: Modifier = Modifier,
    onStartOver: () -> Unit = {},
    minutesValidation: MinutesValidation = validateMinutes(minutesText),
    subjectError: String? = subjectErrorMessage(subject)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .testTag(FocusPlanTestTags.SCREEN),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Capping the content width keeps the fields comfortable on tablets and
        // on a phone in landscape instead of stretching them edge to edge.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 560.dp)
        ) {
            FocusPlanHeader()

            Spacer(modifier = Modifier.height(24.dp))

            PlanInputPanel(
                subject = subject,
                minutesText = minutesText,
                subjectError = subjectError,
                minutesValidation = minutesValidation,
                canCreatePlan = canCreatePlan,
                onSubjectChange = onSubjectChange,
                onMinutesChange = onMinutesChange,
                onCreatePlan = onCreatePlan
            )

            Spacer(modifier = Modifier.height(20.dp))

            CreatePlanButton(
                enabled = canCreatePlan,
                onClick = onCreatePlan
            )

            Spacer(modifier = Modifier.height(10.dp))

            PlanReadinessHint(
                subject = subject,
                minutesValidation = minutesValidation,
                canCreatePlan = canCreatePlan
            )

            Spacer(modifier = Modifier.height(24.dp))

            // AnimatedContent keeps the outgoing card on screen just long
            // enough to fade it out, then shows the placeholder. When `plan`
            // is null (before the first plan, or after an edit) the card is
            // simply not part of the composition at all.
            AnimatedContent(
                targetState = plan,
                transitionSpec = {
                    (fadeIn(tween(260)) + slideInVertically(tween(260)) { it / 6 })
                        .togetherWith(fadeOut(tween(140)))
                },
                label = "result_card"
            ) { targetPlan ->
                if (targetPlan != null) {
                    FocusPlanResultCard(
                        plan = targetPlan,
                        onStartOver = onStartOver
                    )
                } else {
                    ResultPlaceholder()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/** Paints the theme background behind a preview so dark previews look right. */
@Composable
private fun PreviewSurface(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = content
    )
}

// ---------------------------------------------------------------------------
// Previews (Android Studio "Split"/"Design" view). These are the payoff of
// state hoisting: the screen can be rendered in any state without a device.
// ---------------------------------------------------------------------------

@Preview(name = "Empty", showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun FocusPlanScreenEmptyPreview() {
    FocusPlanBuilderTheme {
        PreviewSurface {
            FocusPlanScreen(
                subject = "",
                minutesText = "",
                plan = null,
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = false,
                onCreatePlan = {}
            )
        }
    }
}

@Preview(name = "Invalid minutes", showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun FocusPlanScreenInvalidPreview() {
    FocusPlanBuilderTheme {
        PreviewSurface {
            FocusPlanScreen(
                subject = "Kotlin",
                minutesText = "181",
                plan = null,
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = false,
                onCreatePlan = {}
            )
        }
    }
}

@Preview(name = "With plan", showBackground = true, widthDp = 411, heightDp = 1000)
@Composable
private fun FocusPlanScreenWithPlanPreview() {
    FocusPlanBuilderTheme {
        PreviewSurface {
            FocusPlanScreen(
                subject = "Compose State",
                minutesText = "45",
                plan = FocusPlan(
                    subject = "Compose State",
                    minutes = 45,
                    category = "Focused session",
                    breakMinutes = 10
                ),
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = true,
                onCreatePlan = {}
            )
        }
    }
}

@Preview(
    name = "Dark, with plan",
    showBackground = true,
    widthDp = 411,
    heightDp = 1000,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FocusPlanScreenDarkPreview() {
    FocusPlanBuilderTheme(darkTheme = true) {
        PreviewSurface {
            FocusPlanScreen(
                subject = "Databases",
                minutesText = "90",
                plan = FocusPlan(
                    subject = "Databases",
                    minutes = 90,
                    category = "Extended session",
                    breakMinutes = 15
                ),
                onSubjectChange = {},
                onMinutesChange = {},
                canCreatePlan = true,
                onCreatePlan = {}
            )
        }
    }
}
