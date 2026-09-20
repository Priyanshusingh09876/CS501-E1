package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.FocusPlan
import com.priyanshusingh.focusplanbuilder.model.totalMinutes

/**
 * A proportional bar showing the study block followed by the break, so the
 * ratio between the two is visible at a glance. Row weights do the maths:
 * the study segment weighs [FocusPlan.minutes], the break weighs
 * [FocusPlan.breakMinutes], and both are guaranteed positive for a valid plan.
 */
@Composable
fun SessionTimeline(
    plan: FocusPlan,
    studyColor: Color,
    modifier: Modifier = Modifier
) {
    val breakColor = MaterialTheme.colorScheme.tertiary

    Column(
        modifier = modifier.semantics {
            contentDescription =
                "Timeline: ${plan.minutes} minutes of study, then a ${plan.breakMinutes} minute break, " +
                    "${plan.totalMinutes} minutes in total."
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Session timeline",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${plan.totalMinutes} min total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(plan.minutes.toFloat())
                    .fillMaxHeight()
                    .background(studyColor)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Box(
                modifier = Modifier
                    .weight(plan.breakMinutes.toFloat())
                    .fillMaxHeight()
                    .background(breakColor)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LegendItem(color = studyColor, text = "Study · ${plan.minutes} min")
            LegendItem(color = breakColor, text = "Break · ${plan.breakMinutes} min")
        }
    }
}

@Composable
private fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
