package com.priyanshusingh.focusplanbuilder.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.priyanshusingh.focusplanbuilder.model.QUICK_PICK_MINUTES
import com.priyanshusingh.focusplanbuilder.ui.FocusPlanTestTags
import com.priyanshusingh.focusplanbuilder.ui.theme.PillShape

/**
 * One-tap duration presets.
 *
 * A chip is "selected" purely because the current [minutesText] parses to its
 * value; there is no separate selection state to keep in sync. Tapping a chip
 * simply calls [onMinutesChange] with that number as text, exactly as if the
 * user had typed it, so the reset behaviour and validation apply unchanged.
 */
@Composable
fun QuickPickChips(
    minutesText: String,
    onMinutesChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMinutes: Int? = minutesText.toIntOrNull()

    Column(modifier = modifier) {
        Text(
            text = "Quick picks",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QUICK_PICK_MINUTES.forEach { preset ->
                val selected = currentMinutes == preset
                FilterChip(
                    selected = selected,
                    onClick = { onMinutesChange(preset.toString()) },
                    label = { Text("$preset min") },
                    shape = PillShape,
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = MaterialTheme.colorScheme.outlineVariant,
                        selectedBorderColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.testTag("${FocusPlanTestTags.QUICK_PICK_PREFIX}$preset")
                )
            }
        }
    }
}
