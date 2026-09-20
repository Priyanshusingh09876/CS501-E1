package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.DurationBand
import com.priyanshusingh.focusplanbuilder.model.MAX_MINUTES
import com.priyanshusingh.focusplanbuilder.model.MIN_MINUTES
import com.priyanshusingh.focusplanbuilder.ui.theme.categoryColors

/**
 * A compact legend of the three duration bands with the band matching
 * [minutes] highlighted, plus a thin track showing where the value sits inside
 * the 10–180 window. Everything is derived from [minutes]; when it is null or
 * out of range, no band is highlighted and the track is empty.
 */
@Composable
fun DurationScale(
    minutes: Int?,
    modifier: Modifier = Modifier
) {
    val activeBand = minutes?.let(DurationBand::fromMinutes)

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DurationBand.entries.forEach { band ->
                BandTile(
                    band = band,
                    active = band == activeBand,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        PositionTrack(minutes = minutes, activeBand = activeBand)
    }
}

@Composable
private fun BandTile(
    band: DurationBand,
    active: Boolean,
    modifier: Modifier = Modifier
) {
    val colors = categoryColors(band)
    val container by animateColorAsState(
        targetValue = if (active) colors.container else MaterialTheme.colorScheme.surfaceContainerLowest,
        animationSpec = tween(200),
        label = "band_container"
    )
    val border by animateColorAsState(
        targetValue = if (active) colors.accent else MaterialTheme.colorScheme.outlineVariant,
        animationSpec = tween(200),
        label = "band_border"
    )
    val textColor = if (active) colors.onContainer else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(container)
            .border(
                width = if (active) 1.5.dp else 1.dp,
                color = border,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Text(
            text = band.shortLabel,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Medium,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = band.rangeLabel,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

/**
 * A slim horizontal track. The filled portion grows from the left in
 * proportion to where [minutes] falls between 10 and 180 and takes the colour
 * of the active band.
 */
@Composable
private fun PositionTrack(
    minutes: Int?,
    activeBand: DurationBand?
) {
    val span = (MAX_MINUTES - MIN_MINUTES).toFloat()
    val targetFraction = when {
        minutes == null || activeBand == null -> 0f
        else -> ((minutes - MIN_MINUTES) / span).coerceIn(0.02f, 1f)
    }
    val fraction by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(280),
        label = "track_fraction"
    )
    val fillColor = categoryColors(activeBand).accent

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            if (fraction > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(3.dp))
                        .background(fillColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$MIN_MINUTES min",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$MAX_MINUTES min",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
