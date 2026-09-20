package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.MAX_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MIN_MINUTES
import com.priyanshusingh.focusplanbuilder.ui.theme.Amber70

/**
 * Screen title block: a small brand mark, the title, and one or two sentences
 * of instructions. Split into its own composable so FocusPlanScreen stays a
 * short list of named sections.
 */
@Composable
fun FocusPlanHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark()
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Focus Plan Builder",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Build a study plan you'll actually finish.",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter what you're studying and how many minutes you have " +
                "($MIN_MINUTES–$MAX_MINUTES). Tap Create plan to get a session " +
                "type, a recommended break, and a one-line summary.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * The app's mark: a rounded indigo tile with two concentric rings and an amber
 * centre point, drawn with Canvas so it is crisp at any density and needs no
 * image asset.
 */
@Composable
private fun BrandMark(modifier: Modifier = Modifier) {
    val ringColor = MaterialTheme.colorScheme.onPrimary
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer
        )
    )

    Box(
        modifier = modifier
            .size(36.dp)
            .background(gradient, RoundedCornerShape(11.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(22.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            drawCircle(
                color = ringColor,
                radius = size.minDimension / 2f - 1.5.dp.toPx(),
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )
            drawCircle(
                color = ringColor,
                radius = size.minDimension / 4f,
                center = center,
                style = Stroke(width = 1.5.dp.toPx())
            )
            drawCircle(
                color = Amber70,
                radius = 2.6.dp.toPx(),
                center = center
            )
        }
    }
}
