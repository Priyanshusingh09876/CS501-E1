package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.band
import com.priyanshusingh.focusplanbuilder.model.breakLine
import com.priyanshusingh.focusplanbuilder.model.buildSummary
import com.priyanshusingh.focusplanbuilder.model.categoryLine
import com.priyanshusingh.focusplanbuilder.model.durationLine
import com.priyanshusingh.focusplanbuilder.model.totalMinutes
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import com.priyanshusingh.focusplanbuilder.ui.theme.PanelShape
import com.priyanshusingh.focusplanbuilder.ui.theme.PillShape
import com.priyanshusingh.focusplanbuilder.ui.theme.categoryColors

/**
 * The Material 3 [Card] shown once a valid plan exists.
 *
 * It is only ever composed when FocusPlanScreen's `plan` is non-null, which is
 * what guarantees "the result card should not be visible before the first plan
 * is created": no plan object, no card, no visibility flag to get out of sync.
 *
 * Shows, in order: a category badge, the cleaned subject, three stat tiles, the
 * three required detail lines ("Duration: …", "Category: …", "Recommended
 * break: …"), a proportional session timeline, the complete summary sentence,
 * a one-line tip for the band, and a Start over action.
 */
@Composable
fun FocusPlanResultCard(
    plan: FocusPlan,
    onStartOver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = categoryColors(plan.band)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag(FocusPlanTestTags.RESULT_CARD),
        shape = PanelShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Accent stripe: the category, expressed as colour.
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colors.accent)
            )

            Column(modifier = Modifier.padding(start = 18.dp, top = 20.dp, end = 20.dp, bottom = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your focus plan",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    CategoryBadge(
                        label = plan.category,
                        container = colors.container,
                        onContainer = colors.onContainer
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = plan.subject,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag(FocusPlanTestTags.RESULT_SUBJECT)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PlanStatTile(
                        value = "${plan.minutes}",
                        caption = "min study",
                        valueColor = colors.accent,
                        modifier = Modifier.weight(1f)
                    )
                    PlanStatTile(
                        value = "${plan.breakMinutes}",
                        caption = "min break",
                        valueColor = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                    PlanStatTile(
                        value = "${plan.totalMinutes}",
                        caption = "min total",
                        valueColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                PlanDetailLine(
                    text = durationLine(plan),
                    modifier = Modifier.testTag(FocusPlanTestTags.RESULT_DURATION)
                )
                Spacer(modifier = Modifier.height(6.dp))
                PlanDetailLine(
                    text = categoryLine(plan),
                    modifier = Modifier.testTag(FocusPlanTestTags.RESULT_CATEGORY)
                )
                Spacer(modifier = Modifier.height(6.dp))
                PlanDetailLine(
                    text = breakLine(plan),
                    modifier = Modifier.testTag(FocusPlanTestTags.RESULT_BREAK)
                )

                Spacer(modifier = Modifier.height(18.dp))

                SessionTimeline(plan = plan, studyColor = colors.accent)

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = buildSummary(plan),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag(FocusPlanTestTags.RESULT_SUMMARY)
                )

                plan.band?.let { band ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = band.tip,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onStartOver,
                        modifier = Modifier.testTag(FocusPlanTestTags.START_OVER_BUTTON)
                    ) {
                        Text("Start over")
                    }
                }
            }
        }
    }
}

/** Small tinted pill carrying the category label. */
@Composable
private fun CategoryBadge(
    label: String,
    container: Color,
    onContainer: Color
) {
    Surface(
        shape = PillShape,
        color = container,
        contentColor = onContainer
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

/**
 * Renders "Label: value" as a single Text node with the label muted and the
 * value emphasised. Keeping it one node means the full string (for example
 * "Duration: 45 minutes") stays intact for accessibility and for UI tests.
 */
@Composable
private fun PlanDetailLine(
    text: String,
    modifier: Modifier = Modifier
) {
    val separator = text.indexOf(':')
    val styled = buildAnnotatedString {
        if (separator == -1) {
            append(text)
        } else {
            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                append(text.substring(0, separator + 1))
            }
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(text.substring(separator + 1))
            }
        }
    }

    Text(
        text = styled,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}

/**
 * What sits in the card's place before a plan exists: a quiet hint, not a
 * card, so the "result card is not visible before the first plan" rule holds.
 */
@Composable
fun ResultPlaceholder(modifier: Modifier = Modifier) {
    Text(
        text = "Your plan will appear here.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.outline,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}
