package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.buildSummary
import com.priyanshusingh.focusplanbuilder.ui.theme.categoryAccentColor

/**
 * The Material 3 result card shown after a valid plan has been created.
 *
 * This composable is only ever invoked by FocusPlanScreen when its `plan`
 * parameter is non-null -- see the `if (plan != null)` check there. That
 * is what satisfies the requirement that "the result card should not be
 * visible before the first plan is created": there is no internal
 * visibility flag here to get out of sync: no plan object, no card.
 *
 * The colored stripe down the left edge is not decoration: its color
 * (from [categoryAccentColor]) tells you at a glance which kind of session
 * this is, the same information as the "Category:" line below it, in a
 * second, faster-to-scan form.
 */
@Composable
fun FocusPlanResultCard(
    plan: FocusPlan,
    modifier: Modifier = Modifier
) {
    val accentColor = categoryAccentColor(plan.category)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
            // The accent stripe: a thin colored column running the full
            // height of the card's content.
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = plan.subject,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Duration: ${plan.minutes} minutes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Category: ${plan.category}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Recommended break: ${plan.breakMinutes} minutes",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = buildSummary(plan),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor
                )
            }
        }
    }
}
